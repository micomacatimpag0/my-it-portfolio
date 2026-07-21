// --- ARDUINO SKETCH WITH SERIAL MONITOR LOGS (Optimized Sonar) ---

#include <SPI.h>
#include <Ethernet2.h>
#include <ArduinoHttpClient.h>
#include <NewPing.h>

// --- RAIN GAUGE VARIABLES ---
volatile int rainCount = 0;
float mmPerTip = 0.2794;
const int rainPin = 2;

unsigned long lastRainSendTime = 0;
const unsigned long rainSendInterval = 2000;

float currentRainfall = 0;
unsigned long lastRainUpdate = 0;
unsigned long lastDecayTime = 0;
const unsigned long rainfallDecayInterval = 9000;
const float decayPerInterval = 0.2;

const float MAX_WATER_HEIGHT_CM = 63.0; // adjust based on your physical setup

// --- ETHERNET + HTTP ---
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress serverIP(192, 168, 254, 137);
int serverPort = 3000;

EthernetClient ethernetClient;
HttpClient client(ethernetClient, serverIP, serverPort);
bool ethernetConnected = false;

// --- PIN ASSIGNMENTS ---
const int switchLow  = A3;
const int switchMid  = A4;
const int switchHigh = A5;
const int ledLow     = 9;
const int ledMid     = 7;
const int ledHigh    = 8;
const int buzzerPin  = A1;
bool isBuzzerOn = false;

// --- STATE TRACKERS ---
bool hasSentAllAlert = false;

// --- FLOW SENSOR ---
const int flowSensorPin = 3;
volatile int pulseCount = 0;
float flowRate;
unsigned long oldTime = 0;

// --- TIMING VARIABLES ---
unsigned long lastSMSTime = 0;
unsigned long lastDistanceUpdateTime = 0;
unsigned long lastLoopTime = 0;

const unsigned long smsCooldown = 2UL * 60UL * 1000UL; // 5 minutes
const unsigned long distanceUpdateInterval = 500;      // 0.2 sec (fast sonar updates)
const unsigned long loopInterval = 100UL;

unsigned long lastLowSMSTime = 0;
unsigned long lastMidSMSTime = 0;
const unsigned long lowMidCooldown = 12UL * 60UL * 60UL * 1000UL;

// --- Buzzer Timing ---
bool buzzerActive = false;
unsigned long buzzerStartTime = 0;
unsigned long buzzerCooldownStart = 0;
const unsigned long buzzerDuration = 1UL * 60UL * 1000UL;  // 2 minutes
const unsigned long buzzerCooldown = 5UL * 60UL * 1000UL; // 10 minutes
// --- Non-blocking buzzer pattern state ---
// For single beep
unsigned long lastBeepTimeOnce = 0;
int beepStepOnce = 0;

// For double beep
unsigned long lastBeepTimePattern = 0;
int beepStepPattern = 0;


// --- SONAR SETUP ---
#define TRIGGER_PIN 5
#define ECHO_PIN 6
#define MAX_DISTANCE 500
NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

void setup() {
  Serial.begin(9600);

  pinMode(flowSensorPin, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(flowSensorPin), countPulse, FALLING);

  pinMode(rainPin, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(rainPin), countRain, FALLING);

  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    ethernetConnected = false;
  } else {
    Serial.print("IP Address: ");
    Serial.println(Ethernet.localIP());
    ethernetConnected = true;
  }

  pinMode(switchLow, INPUT_PULLUP);
  pinMode(switchMid, INPUT_PULLUP);
  pinMode(switchHigh, INPUT_PULLUP);

  pinMode(ledLow, OUTPUT);
  pinMode(ledMid, OUTPUT);
  pinMode(ledHigh, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  Serial.println("System Initialized");
}

void loop() {
  unsigned long currentMillis = millis();

  if (currentMillis - lastLoopTime >= loopInterval) {
    lastLoopTime = currentMillis;

    bool isLow = digitalRead(switchLow) == HIGH;
    bool isMid = digitalRead(switchMid) == HIGH;
    bool isHigh = digitalRead(switchHigh) == HIGH;

    digitalWrite(ledLow, isLow ? HIGH : LOW);
    digitalWrite(ledMid, isMid ? HIGH : LOW);
    digitalWrite(ledHigh, isHigh ? HIGH : LOW);

    // --- ALERT LOGIC ---
    if (isLow && isMid && isHigh) {
      if (!hasSentAllAlert || (currentMillis - lastSMSTime >= smsCooldown)) {
        if (ethernetConnected) {
          Serial.println("Sending FLOOD ALERT SMS...");
          sendSMSRequest("🚨 FLOOD ALERT: All float switches are triggered!");
          sendFloodTrigger();
        }

        lastSMSTime = currentMillis;
        hasSentAllAlert = true;
      }
      handleBuzzer(beepLong, isLow && isMid && isHigh);

    } else {
      digitalWrite(buzzerPin, LOW);
      isBuzzerOn = false;
      if (!(isLow && isMid && isHigh)) hasSentAllAlert = false;

      if (isLow && isHigh && !isMid && (currentMillis - lastMidSMSTime >= smsCooldown)) {
        if (ethernetConnected) {
          Serial.println("Sending MID SMS...");
          sendMidSMSRequest("⚠️ MID switch triggered: Moderate water level detected.");
          lastMidSMSTime = currentMillis;
        }
      }

      if (isLow && isHigh && !isMid) {
        handleBuzzer(beepPattern, isLow && isHigh && !isMid);
      }
      if (isLow && !isMid && !isHigh) {
        handleBuzzer(beeponce, isLow && !isMid && !isHigh);  
      }

      if (isLow && !isMid && !isHigh && (currentMillis - lastLowSMSTime >= smsCooldown)) {
        if (ethernetConnected) {
          Serial.println("Sending LOW SMS...");
          sendLowSMSRequest("⚠️ LOW switch triggered: Possible water rising.");
          lastLowSMSTime = currentMillis;
        }
      }
    }

    // --- SONAR WATER LEVEL (FAST UPDATES) ---
    if (currentMillis - lastDistanceUpdateTime >= distanceUpdateInterval) {
      float waterHeight = readSonar();
      if (waterHeight != -1) {
        Serial.print("Sonar Water Height: ");
        Serial.print(waterHeight);
        Serial.println(" cm");
      } else {
        Serial.println("Sonar: No valid reading");
      }

      if (!isnan(waterHeight) && ethernetConnected) sendDistance(waterHeight);
      lastDistanceUpdateTime = currentMillis;
    }

    // --- RAINFALL ---
    static unsigned long lastRainProcessTime = 0;
    if (millis() - lastRainProcessTime >= 1000) {
      lastRainProcessTime = millis();
      noInterrupts(); int countCopy = rainCount; rainCount = 0; interrupts();
      if (countCopy > 0) {
        currentRainfall += countCopy * mmPerTip;
        lastRainUpdate = millis();
        Serial.print("Rainfall: ");
        Serial.print(currentRainfall, 2);
        Serial.print(" mm | Intensity: ");
        Serial.println(classifyRainfall(currentRainfall));
      }
    }

    if (currentMillis - lastRainUpdate >= rainfallDecayInterval &&
        currentMillis - lastDecayTime >= rainfallDecayInterval &&
        currentRainfall > 0) {
      currentRainfall = max(0.0, currentRainfall - decayPerInterval);
      lastDecayTime = currentMillis;
    }

    if (ethernetConnected && (currentMillis - lastRainSendTime >= rainSendInterval)) {
      if (!isnan(currentRainfall)) sendRainfall(currentRainfall);
      lastRainSendTime = currentMillis;
    }

    // --- FLOW SENSOR ---
    if (millis() - oldTime > 1000) {
      detachInterrupt(digitalPinToInterrupt(flowSensorPin));
      flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / 7.5;
      if (pulseCount > 0) {
        Serial.print("Flow Rate: ");
        Serial.print(flowRate, 2);
        Serial.println(" L/min");
      }
      if (!isnan(flowRate) && pulseCount > 0 && ethernetConnected) sendFlowRate(flowRate);
      pulseCount = 0; oldTime = millis();
      attachInterrupt(digitalPinToInterrupt(flowSensorPin), countPulse, FALLING);
    }
  }
}

void countRain() { rainCount++; }
void countPulse() { pulseCount++; }

String escapeJSONString(String input) {
  input.replace("\\", "\\\\");
  input.replace("\"", "\\\"");
  return input;
}

float readSonar() {
  static float lastValidHeight = 0;  // stores last good value

  delay(30);
  unsigned int uS = sonar.ping();
  float distance = uS / US_ROUNDTRIP_CM;

  if (distance == 0 || distance > MAX_WATER_HEIGHT_CM) {
    return lastValidHeight; // keep old value if invalid
  }

  float waterHeight = MAX_WATER_HEIGHT_CM - distance;
  lastValidHeight = waterHeight;
  return waterHeight;
}


// --- Buzzer Alerts ---
void beeponce() {
  unsigned long now = millis();

  switch (beepStepOnce) {
    case 0:
      digitalWrite(buzzerPin, HIGH);
      lastBeepTimeOnce = now;
      beepStepOnce = 1;
      break;
    case 1:
      if (now - lastBeepTimeOnce >= 100) {
        digitalWrite(buzzerPin, LOW);
        lastBeepTimeOnce = now;
        beepStepOnce = 2;
      }
      break;
    case 2:
      if (now - lastBeepTimeOnce >= 2850) {
        beepStepOnce = 0;
      }
      break;
  }
}

// --- Non-blocking double beep every ~2s ---
void beepPattern() {
  unsigned long now = millis();

  switch (beepStepPattern) {
    case 0: // first beep ON
      digitalWrite(buzzerPin, HIGH);
      lastBeepTimePattern = now;
      beepStepPattern = 1;
      break;

    case 1: // first beep OFF
      if (now - lastBeepTimePattern >= 100) { // 100ms ON
        digitalWrite(buzzerPin, LOW);
        lastBeepTimePattern = now;
        beepStepPattern = 2;
      }
      break;

    case 2: // short gap before 2nd beep
      if (now - lastBeepTimePattern >= 100) { // 100ms gap
        digitalWrite(buzzerPin, HIGH);
        lastBeepTimePattern = now;
        beepStepPattern = 3;
      }
      break;

    case 3: // second beep OFF
      if (now - lastBeepTimePattern >= 100) { // 100ms ON
        digitalWrite(buzzerPin, LOW);
        lastBeepTimePattern = now;
        beepStepPattern = 4;
      }
      break;

    case 4: // long gap before repeating
      if (now - lastBeepTimePattern >= 1850) { // ~1.85s gap
        beepStepPattern = 0; // restart cycle
      }
      break;
  }
}


// --- Long Beep Pattern (3s ON, 1s OFF) ---
void beepLong() {
  unsigned long now = millis();
  static unsigned long lastToggle = 0;
  static bool isOn = false;

  if (isOn && (now - lastToggle >= 3000)) {  // 3s ON → turn OFF
    digitalWrite(buzzerPin, LOW);
    isOn = false;
    lastToggle = now;
  } 
  else if (!isOn && (now - lastToggle >= 1000)) {  // 1s OFF → turn ON
    digitalWrite(buzzerPin, HIGH);
    isOn = true;
    lastToggle = now;
  }
}


// --- Buzzer Timing Controller with Reset ---
void handleBuzzer(void (*pattern)(), bool active) {
  unsigned long now = millis();

  // If switch goes LOW → reset everything (including cooldown)
  if (!active) {
    if (buzzerActive) {
      Serial.println("Buzzer stopped (float inactive, reset cooldown)");
    }
    buzzerActive = false;
    digitalWrite(buzzerPin, LOW);
    buzzerStartTime = 0;
    buzzerCooldownStart = 0; // ✅ reset cooldown
    return;
  }

  // If switch HIGH and buzzer not active → start new cycle immediately
  if (!buzzerActive) {
    buzzerActive = true;
    buzzerStartTime = now;
    Serial.println("Buzzer started (new cycle)");
  }

  // If active and within duration → run beep pattern
  if (buzzerActive && (now - buzzerStartTime <= buzzerDuration)) {
    pattern();
  }

  // If duration expired → stop and start cooldown
  else if (buzzerActive && (now - buzzerStartTime > buzzerDuration)) {
    buzzerActive = false;
    buzzerCooldownStart = now;
    digitalWrite(buzzerPin, LOW);
    Serial.println("Buzzer stopped (cooldown started)");
  }
}


// --- Rainfall Classification ---
String classifyRainfall(float value) {
  if (value == 0) return "None";
  else if (value < 2.5) return "Light";
  else if (value < 7.6) return "Moderate";
  else return "Heavy";
}

// --- HTTP SEND FUNCTIONS ---
void sendDistance(float height) {
  String path = "/update-water-level";
  String body = "{\"water_height_cm\": " + String(height, 2) + "}";

  client.beginRequest();
  client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody();
  client.print(body);
  client.endRequest();
  client.stop();
  delay(100);

}

void sendFlowRate(float flowRate) {
  String path = "/update-flow";
  String body = "{\"flow_lpm\": " + String(flowRate, 2) + "}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}

void sendRainfall(float rainfall) {
  String intensity = classifyRainfall(rainfall);
  String path = "/update-rain";
  String body = "{\"rain_mm\": " + String(rainfall, 2) + ", \"intensity\": \"" + intensity + "\"}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}

void sendFloodTrigger() {
  String path = "/report-flood";
  String body = "{\"location\": \"Brgy. Siha\", \"recipient\": \"Barangay Office\"}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}

void sendSMSRequest(String alertMessage) {
  String path = "/send-sms";
  String body = "{\"alert\": \"" + escapeJSONString(alertMessage) + "\"}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}

void sendLowSMSRequest(String alertMessage) {
  String path = "/report-low";
  String body = "{\"alert\": \"" + escapeJSONString(alertMessage) + "\"}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}

void sendMidSMSRequest(String alertMessage) {
  String path = "/report-mid";
  String body = "{\"alert\": \"" + escapeJSONString(alertMessage) + "\"}";
  client.beginRequest(); client.post(path);
  client.sendHeader("Host", "192.168.254.137");
  client.sendHeader("Content-Type", "application/json");
  client.sendHeader("Content-Length", body.length());
  client.beginBody(); client.print(body); client.endRequest();
  client.stop();
  delay(100);

}