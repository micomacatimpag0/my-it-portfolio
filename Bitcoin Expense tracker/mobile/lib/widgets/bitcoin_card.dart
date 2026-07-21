import 'package:flutter/material.dart';

import '../utils/formatter.dart';

class BitcoinCard extends StatelessWidget {
  const BitcoinCard({
    super.key,
    required this.pricePhp,
    this.updatedAt,
  });

  final double pricePhp;
  final String? updatedAt;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.blueGrey.shade900,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            "Current Bitcoin Price",
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey,
            ),
          ),
          const SizedBox(height: 8),
          Text(
            AppFormatter.php(pricePhp),
            style: const TextStyle(
              fontSize: 32,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            updatedAt == null ? "BTC PHP" : "BTC PHP • $updatedAt",
            style: const TextStyle(color: Colors.grey),
          ),
        ],
      ),
    );
  }
}
