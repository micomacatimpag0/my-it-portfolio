class AppConstants {
  static const String appTitle = "Bitcoin Alert";
  static const String userId = "testUser";
  static const String bitcoinPath = "bitcoin";
  static const String usersPath = "users";
  static const String alertsPath = "alerts";
  static const String btcAlertPath = "btc";
  static const String transactionsPath = "transactions";

  static String get userPath => "$usersPath/$userId";
  static String get btcAlertFullPath => "$userPath/$alertsPath/$btcAlertPath";
  static String get transactionsFullPath => "$userPath/$transactionsPath";
}
