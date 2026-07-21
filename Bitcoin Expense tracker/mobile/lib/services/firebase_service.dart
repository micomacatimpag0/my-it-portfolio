import 'package:firebase_database/firebase_database.dart';

import '../utils/constants.dart';

class FirebaseService {
  FirebaseService() : database = FirebaseDatabase.instance.ref();

  final DatabaseReference database;

  DatabaseReference get bitcoin => database.child(AppConstants.bitcoinPath);

  DatabaseReference get btcAlert =>
      database.child(AppConstants.btcAlertFullPath);

  DatabaseReference get transactions =>
      database.child(AppConstants.transactionsFullPath);
}
