import 'package:intl/intl.dart';

class AppFormatter {
  static final _phpFormatter = NumberFormat('#,##0.00');

  static String php(num value) {
    return "₱${_phpFormatter.format(value)}";
  }

  static String btc(num value) {
    final text = value
        .toStringAsFixed(8)
        .replaceFirst(RegExp(r'\.?0+$'), '');

    return "$text BTC";
  }
  static String percent(num value) {
    final sign = value > 0 ? "+" : "";
    return "$sign${value.toStringAsFixed(2)}%";
  }

  static String shortDate(DateTime date) {
    final month = date.month.toString().padLeft(2, "0");
    final day = date.day.toString().padLeft(2, "0");
    return "${date.year}-$month-$day";
  }

  static double parseDouble(Object? value) {
    return double.tryParse(value?.toString() ?? "") ?? 0;
  }
}
