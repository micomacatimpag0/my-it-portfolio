import 'package:flutter/material.dart';

import '../models/portfolio.dart';
import '../utils/formatter.dart';

class ProfitCard extends StatelessWidget {
  const ProfitCard({
    super.key,
    required this.portfolio,
  });

  final Portfolio portfolio;

  @override
  Widget build(BuildContext context) {
    final isProfit = portfolio.profit >= 0;
    final color = isProfit ? Colors.green : Colors.red;

    return Card(
      child: ListTile(
        leading: Icon(
          isProfit ? Icons.arrow_upward : Icons.arrow_downward,
          color: color,
        ),
        title: const Text("Profit / Loss"),
        subtitle: Text(AppFormatter.percent(portfolio.profitPercent)),
        trailing: Text(
          AppFormatter.php(portfolio.profit),
          style: TextStyle(
            color: color,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
