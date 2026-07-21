import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'firebase_options.dart';
import 'screens/dashboard_screen.dart';
import 'services/notification_service.dart';


Future<void> setupNotifications() async {

  FirebaseMessaging messaging = FirebaseMessaging.instance;


  NotificationSettings settings =
      await messaging.requestPermission();


  if (settings.authorizationStatus ==
      AuthorizationStatus.authorized) {


    String? token =
        await messaging.getToken();


    print("FCM TOKEN:");
    print(token);


  }

}


void main() async {

  WidgetsFlutterBinding.ensureInitialized();


  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );


  await setupNotifications();


  await NotificationService().saveToken();


  runApp(const BitcoinAlertApp());

}



class BitcoinAlertApp extends StatelessWidget {

  const BitcoinAlertApp({super.key});


  @override
  Widget build(BuildContext context) {

    return MaterialApp(

      debugShowCheckedModeBanner: false,

      title: "Bitcoin Alert",

      theme: ThemeData(

        brightness: Brightness.dark,

        useMaterial3: true,

      ),

      home: DashboardScreen(),

    );

  }

}
