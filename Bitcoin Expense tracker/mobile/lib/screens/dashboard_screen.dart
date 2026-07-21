import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';

import '../models/portfolio.dart';
import '../models/transaction.dart';
import '../services/bitcoin_service.dart';
import '../services/portfolio_service.dart';
import '../utils/formatter.dart';
import '../widgets/bitcoin_card.dart';
import '../widgets/portfolio_card.dart';
import '../widgets/profit_card.dart';
import '../widgets/transaction_tile.dart';
import 'add_transaction_screen.dart';
import 'alerts_screen.dart';
import 'portfolio_screen.dart';
import 'settings_screen.dart';
import 'transaction_history_screen.dart';

class DashboardScreen extends StatelessWidget {
  DashboardScreen({super.key});

  final BitcoinService _bitcoinService = BitcoinService();
  final PortfolioService _portfolioService = PortfolioService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Bitcoin Alert"),
        centerTitle: true,
        actions: [
          IconButton(
            tooltip: "Settings",
            icon: const Icon(Icons.settings),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const SettingsScreen()),
              );
            },
          ),
        ],
      ),
      body: StreamBuilder<DatabaseEvent>(
        stream: _bitcoinService.getBitcoinData(),

        builder: (context, bitcoinSnapshot) {

          final bitcoinData =
              bitcoinSnapshot.data?.snapshot.value
              as Map<dynamic, dynamic>?;


          final currentPrice =
              bitcoinData == null
              ? 0.0
              : AppFormatter.parseDouble(
                  bitcoinData["lastPrice"],
                );


          return StreamBuilder<DatabaseEvent>(
            stream: _portfolioService.getTransactions(),

            builder: (context, transactionSnapshot) {

              final List<BitcoinTransaction> transactions =
                  transactionSnapshot.hasData
                  ? _portfolioService.transactionsFromSnapshot(
                      transactionSnapshot.data!.snapshot,
                    )
                  : <BitcoinTransaction>[];


              final portfolio =
                  Portfolio.fromTransactions(
                    transactions,
                    currentPrice,
                  );


              return ListView(
                padding: const EdgeInsets.all(20),
                children: [
                  BitcoinCard(
                    pricePhp: currentPrice,
                    updatedAt: bitcoinData?["updatedAt"]?.toString(),
                  ),
                  const SizedBox(height: 20),
                  _SectionHeader(
                    title: "My Portfolio",
                    onViewAll: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => PortfolioScreen()),
                      );
                    },
                  ),
                  PortfolioCard(portfolio: portfolio),
                  ProfitCard(portfolio: portfolio),
                  const SizedBox(height: 20),
                  _ActionGrid(
                    onAddTransaction: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const AddTransactionScreen(),
                        ),
                      );
                    },
                    onAlerts: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => const AlertsScreen()),
                      );
                    },
                  ),
                  const SizedBox(height: 20),
                  _SectionHeader(
                    title: "Recent Transactions",
                    onViewAll: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => TransactionHistoryScreen(),
                        ),
                      );
                    },
                  ),
                  if (transactions.isEmpty)
                    const Card(
                      child: ListTile(
                        leading: Icon(Icons.receipt_long),
                        title: Text("No transactions yet"),
                        subtitle: Text("Add a buy or sell to start tracking"),
                      ),
                    )
                  else
                    ...transactions
                        .take(3)
                        .map((transaction) => TransactionTile(
                              transaction: transaction,
                            )),
                ],
              );
            },
          );
        },
      ),
    );
  }
}

class _SectionHeader extends StatelessWidget {
  const _SectionHeader({
    required this.title,
    required this.onViewAll,
  });

  final String title;
  final VoidCallback onViewAll;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Text(
            title,
            style: const TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        TextButton(
          onPressed: onViewAll,
          child: const Text("View"),
        ),
      ],
    );
  }
}

class _ActionGrid extends StatelessWidget {
  const _ActionGrid({
    required this.onAddTransaction,
    required this.onAlerts,
  });

  final VoidCallback onAddTransaction;
  final VoidCallback onAlerts;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: ElevatedButton.icon(
            style: ElevatedButton.styleFrom(
              minimumSize: const Size(double.infinity, 55),
            ),
            onPressed: onAddTransaction,
            icon: const Icon(Icons.add),
            label: const Text("Add Trade"),
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: OutlinedButton.icon(
            style: OutlinedButton.styleFrom(
              minimumSize: const Size(double.infinity, 55),
            ),
            onPressed: onAlerts,
            icon: const Icon(Icons.notifications),
            label: const Text("Alerts"),
          ),
        ),
      ],
    );
  }
}
