import 'package:flutter/material.dart';

import '../models/transaction.dart';
import '../utils/formatter.dart';


class TransactionTile extends StatelessWidget {

  const TransactionTile({

    super.key,

    required this.transaction,

    this.onDelete,

    this.onEdit,

  });



  final BitcoinTransaction transaction;

  final VoidCallback? onDelete;

  final VoidCallback? onEdit;



  @override
  Widget build(BuildContext context) {


    final isBuy = transaction.isBuy;



    return Card(


      child: ListTile(


        leading: Icon(

          isBuy
              ? Icons.add_circle
              : Icons.remove_circle,

          color:

          isBuy
              ? Colors.green
              : Colors.red,

        ),




        title: Text(

          transaction.type.toUpperCase(),

        ),





        subtitle: Text(

          "${AppFormatter.btc(transaction.btcAmount)}\n"

          "Price: ${AppFormatter.php(transaction.btcPrice)} • "

          "${AppFormatter.shortDate(transaction.date)}",

        ),





        trailing:

        onDelete == null && onEdit == null

        ? Text(

            AppFormatter.php(
              transaction.phpAmount
            ),

          )



        : Row(

            mainAxisSize:
            MainAxisSize.min,


            children: [


              IconButton(

                tooltip:
                "Edit",


                icon:
                const Icon(
                  Icons.edit,
                  color: Colors.blue,
                ),


                onPressed:
                onEdit,

              ),




              IconButton(

                tooltip:
                "Delete",


                icon:
                const Icon(
                  Icons.delete_outline,
                  color: Colors.red,
                ),


                onPressed:
                onDelete,

              ),


            ],

          ),


      ),

    );

  }

}