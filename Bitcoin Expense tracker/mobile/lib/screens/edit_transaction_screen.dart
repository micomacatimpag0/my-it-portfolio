import 'package:flutter/material.dart';

import '../models/transaction.dart';
import '../services/portfolio_service.dart';


class EditTransactionScreen extends StatefulWidget {

  const EditTransactionScreen({
    super.key,
    required this.transaction,
  });


  final BitcoinTransaction transaction;


  @override
  State<EditTransactionScreen> createState() =>
      _EditTransactionScreenState();

}



class _EditTransactionScreenState
    extends State<EditTransactionScreen> {


  final PortfolioService _portfolioService =
      PortfolioService();



  late TextEditingController typeController;
  late TextEditingController phpController;
  late TextEditingController btcController;
  late TextEditingController priceController;



  @override
  void initState() {

    super.initState();


    final transaction = widget.transaction;


    typeController =
        TextEditingController(
          text: transaction.type,
        );


    phpController =
        TextEditingController(
          text: transaction.phpAmount.toString(),
        );


    btcController =
        TextEditingController(
          text: transaction.btcAmount.toString(),
        );


    priceController =
        TextEditingController(
          text: transaction.btcPrice.toString(),
        );

  }





  @override
  void dispose() {

    typeController.dispose();

    phpController.dispose();

    btcController.dispose();

    priceController.dispose();


    super.dispose();

  }






  void updateTransaction() async {


    await _portfolioService.updateTransaction(

      widget.transaction.id,

      {

        "type":
        typeController.text,


        "phpAmount":
        double.parse(
          phpController.text,
        ),


        "btcAmount":
        double.parse(
          btcController.text,
        ),


        "btcPrice":
        double.parse(
          priceController.text,
        ),


      },

    );



    if(context.mounted){

      Navigator.pop(context);

    }


  }







  @override
  Widget build(BuildContext context) {


    return Scaffold(


      appBar: AppBar(

        title:
        const Text(
          "Edit Transaction",
        ),

      ),




      body:
      Padding(

        padding:
        const EdgeInsets.all(20),



        child:
        ListView(

          children: [



            TextField(

              controller:
              typeController,

              decoration:
              const InputDecoration(

                labelText:
                "Type (BUY/SELL)",

              ),

            ),




            TextField(

              controller:
              phpController,

              keyboardType:
              TextInputType.number,

              decoration:
              const InputDecoration(

                labelText:
                "PHP Amount",

              ),

            ),





            TextField(

              controller:
              btcController,

              keyboardType:
              TextInputType.number,

              decoration:
              const InputDecoration(

                labelText:
                "BTC Amount",

              ),

            ),





            TextField(

              controller:
              priceController,

              keyboardType:
              TextInputType.number,

              decoration:
              const InputDecoration(

                labelText:
                "BTC Price",

              ),

            ),





            const SizedBox(height:30),





            ElevatedButton(

              onPressed:
              updateTransaction,


              child:
              const Text(
                "Save Changes",
              ),

            ),


          ],

        ),

      ),


    );


  }


}