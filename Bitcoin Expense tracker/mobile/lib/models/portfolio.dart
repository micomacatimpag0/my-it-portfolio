import 'transaction.dart';

class Portfolio {
  const Portfolio({
    required this.totalBtc,
    required this.totalInvested,
    required this.currentValue,
  });

  final double totalBtc;
  final double totalInvested;
  final double currentValue;

  double get profit => currentValue - totalInvested;

  double get profitPercent {
    if (totalInvested == 0) {
      return 0;
    }

    return profit / totalInvested * 100;
  }

  static Portfolio fromTransactions(
    List<BitcoinTransaction> transactions,
    double currentBtcPrice,
  ) {
    var totalBtc = 0.0;
    var totalInvested = 0.0;

    for (final transaction in transactions) {
      if (transaction.isBuy) {
        totalBtc += transaction.btcAmount;
        totalInvested += transaction.phpAmount;
      } else {
        totalBtc -= transaction.btcAmount;
        totalInvested -= transaction.phpAmount;
      }
    }

    return Portfolio(
      totalBtc: totalBtc,
      totalInvested: totalInvested,
      currentValue: totalBtc * currentBtcPrice,
    );
  }
}
