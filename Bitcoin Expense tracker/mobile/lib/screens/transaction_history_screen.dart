import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';

import '../services/portfolio_service.dart';
import '../widgets/transaction_tile.dart';
import 'edit_transaction_screen.dart';


class TransactionHistoryScreen extends StatelessWidget {

  TransactionHistoryScreen({super.key});


  final PortfolioService _portfolioService =
      PortfolioService();



  @override
  Widget build(BuildContext context) {

    return Scaffold(

      appBar: AppBar(

        title:
        const Text(
          "Transaction History"
        ),

      ),



      body:
      StreamBuilder<DatabaseEvent>(


        stream:
        _portfolioService.getTransactions(),



        builder:(context,snapshot){



          if(!snapshot.hasData){

            return const Center(

              child:
              CircularProgressIndicator(),

            );

          }





          final transactions =
          _portfolioService
              .transactionsFromSnapshot(
                snapshot.data!.snapshot,
              );





          if(transactions.isEmpty){

            return const Center(

              child:
              Text(
                "No transactions yet"
              ),

            );

          }






          return ListView.builder(


            padding:
            const EdgeInsets.all(12),



            itemCount:
            transactions.length,



            itemBuilder:(context,index){


              final transaction =
              transactions[index];



              return TransactionTile(


                transaction:
                transaction,



                onDelete:(){

                  _portfolioService
                      .deleteTransaction(
                        transaction.id
                      );

                },



                onEdit:(){


                  Navigator.push(

                    context,

                    MaterialPageRoute(

                      builder:(context)=>
                      EditTransactionScreen(

                        transaction:
                        transaction,

                      ),

                    ),

                  );


                },


              );


            },


          );

        },

      ),

    );

  }

}