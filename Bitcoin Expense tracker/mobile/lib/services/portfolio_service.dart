import 'package:firebase_database/firebase_database.dart';

import '../models/portfolio.dart';
import '../models/transaction.dart';
import 'firebase_service.dart';


class PortfolioService {

  PortfolioService({FirebaseService? firebaseService})
      : _firebaseService = firebaseService ?? FirebaseService();


  final FirebaseService _firebaseService;



  Stream<DatabaseEvent> getTransactions() {

    return _firebaseService.transactions.onValue;

  }




  Future<void> addTransaction(
      BitcoinTransaction transaction
  ) {

    return _firebaseService.transactions
        .push()
        .set(
          transaction.toMap(),
        );

  }





  Future<void> deleteTransaction(
      String transactionId
  ) {

    return _firebaseService.transactions
        .child(transactionId)
        .remove();

  }






  Future<void> updateTransaction(
      String transactionId,
      Map<String, dynamic> data,
  ) {

    return _firebaseService.transactions
        .child(transactionId)
        .update(data);

  }







  List<BitcoinTransaction> transactionsFromSnapshot(
      DataSnapshot snapshot
  ) {

    final data = snapshot.value;



    if (data == null || data is! Map) {

      return [];

    }




    final transactions =
        data.entries.map((entry) {


      return BitcoinTransaction.fromMap(

        entry.key.toString(),

        entry.value as Map<dynamic, dynamic>,

      );


    }).toList();




    transactions.sort(
      (a, b) => b.date.compareTo(a.date),
    );



    return transactions;

  }







  Portfolio calculatePortfolio(

    List<BitcoinTransaction> transactions,

    double currentBtcPrice,

  ) {

    return Portfolio.fromTransactions(

      transactions,

      currentBtcPrice,

    );

  }

}