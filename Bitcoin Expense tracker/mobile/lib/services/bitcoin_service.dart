import 'package:firebase_database/firebase_database.dart';

import 'firebase_service.dart';

class BitcoinService {
  BitcoinService({FirebaseService? firebaseService})
      : _firebaseService = firebaseService ?? FirebaseService();

  final FirebaseService _firebaseService;

  Stream<DatabaseEvent> getBitcoinData() {
    return _firebaseService.bitcoin.onValue;
  }

  Stream<DatabaseEvent> getAlertData() {
    return _firebaseService.btcAlert.onValue;
  }

  double priceFromEvent(DatabaseEvent event) {
    final data = event.snapshot.value as Map<dynamic, dynamic>?;
    return double.tryParse(data?["pricePHP"]?.toString() ?? "") ?? 0;
  }
}
