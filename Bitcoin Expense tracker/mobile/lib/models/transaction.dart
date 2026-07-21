class BitcoinTransaction {
  const BitcoinTransaction({
    required this.id,
    required this.type,
    required this.phpAmount,
    required this.btcAmount,
    required this.btcPrice,
    required this.date,
    this.note = "",
  });

  final String id;
  final String type;
  final double phpAmount;
  final double btcAmount;
  final double btcPrice;
  final DateTime date;
  final String note;

  bool get isBuy => type.toUpperCase() == "BUY";

  factory BitcoinTransaction.fromMap(String id, Map<dynamic, dynamic> data) {
    return BitcoinTransaction(
      id: id,
      type: data["type"]?.toString() ?? "BUY",
      phpAmount: _toDouble(data["phpAmount"]),
      btcAmount: _toDouble(data["btcAmount"]),
      btcPrice: _toDouble(data["btcPrice"]),
      date: DateTime.tryParse(data["date"]?.toString() ?? "") ??
          DateTime.now(),
      note: data["note"]?.toString() ?? "",
    );
  }

  Map<String, dynamic> toMap() {
    return {
      "type": type,
      "phpAmount": phpAmount,
      "btcAmount": btcAmount,
      "btcPrice": btcPrice,
      "date": date.toIso8601String(),
      "note": note,
    };
  }

  static double _toDouble(Object? value) {
    return double.tryParse(value?.toString() ?? "") ?? 0;
  }
}
