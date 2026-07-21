import 'package:firebase_database/firebase_database.dart';

import 'firebase_service.dart';

class AlertService {
  AlertService({FirebaseService? firebaseService})
      : _firebaseService = firebaseService ?? FirebaseService();

  final FirebaseService _firebaseService;

  Stream<DatabaseEvent> getAlert() {
    return _firebaseService.btcAlert.onValue;
  }

  Future<void> saveAlert({
    required double targetPrice,
    required bool enabled,
  }) {
    return _firebaseService.btcAlert.update({
      "targetPrice": targetPrice,
      "enabled": enabled,
      "triggered": false,
    });
  }

  Future<void> setEnabled(bool enabled) {
    return _firebaseService.btcAlert.update({
      "enabled": enabled,
      if (enabled) "triggered": false,
    });
  }
}
