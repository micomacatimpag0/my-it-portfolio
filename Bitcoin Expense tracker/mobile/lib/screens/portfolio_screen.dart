import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';

import '../models/portfolio.dart';
import '../services/bitcoin_service.dart';
import '../services/portfolio_service.dart';
import '../widgets/portfolio_card.dart';
import '../widgets/profit_card.dart';

class PortfolioScreen extends StatelessWidget {
  PortfolioScreen({super.key});

  final BitcoinService _bitcoinService = BitcoinService();
  final PortfolioService _portfolioService = PortfolioService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Portfolio"),
      ),
      body: StreamBuilder<DatabaseEvent>(
        stream: _bitcoinService.getBitcoinData(),
        builder: (context, bitcoinSnapshot) {
          final currentPrice = bitcoinSnapshot.hasData
              ? _bitcoinService.priceFromEvent(bitcoinSnapshot.data!)
              : 0.0;

          return StreamBuilder<DatabaseEvent>(
            stream: _portfolioService.getTransactions(),
            builder: (context, transactionSnapshot) {
              if (!transactionSnapshot.hasData) {
                return const Center(child: CircularProgressIndicator());
              }

              final transactions = _portfolioService.transactionsFromSnapshot(
                transactionSnapshot.data!.snapshot,
              );
              final portfolio = Portfolio.fromTransactions(
                transactions,
                currentPrice,
              );

              return ListView(
                padding: const EdgeInsets.all(20),
                children: [
                  PortfolioCard(portfolio: portfolio),
                  ProfitCard(portfolio: portfolio),
                ],
              );
            },
          );
        },
      ),
    );
  }
}
