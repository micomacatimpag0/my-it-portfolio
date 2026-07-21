import 'package:flutter/material.dart';

import '../models/portfolio.dart';
import '../utils/formatter.dart';

class PortfolioCard extends StatelessWidget {
  const PortfolioCard({
    super.key,
    required this.portfolio,
  });

  final Portfolio portfolio;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            _PortfolioRow(
              icon: Icons.account_balance_wallet,
              iconColor: Colors.orange,
              label: "Total BTC",
              value: AppFormatter.btc(portfolio.totalBtc),
            ),
            const Divider(),
            _PortfolioRow(
              icon: Icons.payments,
              iconColor: Colors.green,
              label: "Total Invested",
              value: AppFormatter.php(portfolio.totalInvested),
            ),
            const Divider(),
            _PortfolioRow(
              icon: Icons.trending_up,
              iconColor: Colors.blue,
              label: "Current Value",
              value: AppFormatter.php(portfolio.currentValue),
            ),
          ],
        ),
      ),
    );
  }
}

class _PortfolioRow extends StatelessWidget {
  const _PortfolioRow({
    required this.icon,
    required this.iconColor,
    required this.label,
    required this.value,
  });

  final IconData icon;
  final Color iconColor;
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      contentPadding: EdgeInsets.zero,
      leading: Icon(icon, color: iconColor),
      title: Text(label),
      subtitle: Text(value),
    );
  }
}
