import 'package:flutter/material.dart';

import '../models/transaction.dart';
import '../services/portfolio_service.dart';

class AddTransactionScreen extends StatefulWidget {
  const AddTransactionScreen({super.key});

  @override
  State<AddTransactionScreen> createState() => _AddTransactionScreenState();
}

class _AddTransactionScreenState extends State<AddTransactionScreen> {
  final _formKey = GlobalKey<FormState>();
  final _portfolioService = PortfolioService();
  final _phpController = TextEditingController();
  final _btcController = TextEditingController();
  final _priceController = TextEditingController();
  final _noteController = TextEditingController();

  String _type = "BUY";
  bool _saving = false;

  @override
  void dispose() {
    _phpController.dispose();
    _btcController.dispose();
    _priceController.dispose();
    _noteController.dispose();
    super.dispose();
  }

  Future<void> _saveTransaction() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    setState(() {
      _saving = true;
    });

    final transaction = BitcoinTransaction(
      id: "",
      type: _type,
      phpAmount: double.parse(_phpController.text),
      btcAmount: double.parse(_btcController.text),
      btcPrice: double.parse(_priceController.text),
      date: DateTime.now(),
      note: _noteController.text.trim(),
    );

    await _portfolioService.addTransaction(transaction);

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text("Transaction saved")),
    );
    Navigator.pop(context);
  }

  String? _requiredNumber(String? value) {
    final number = double.tryParse(value ?? "");

    if (number == null || number <= 0) {
      return "Enter a valid amount";
    }

    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Add Transaction"),
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: const EdgeInsets.all(20),
          children: [
            DropdownButtonFormField<String>(
              value: _type,
              decoration: const InputDecoration(labelText: "Type"),
              items: const [
                DropdownMenuItem(value: "BUY", child: Text("BUY")),
                DropdownMenuItem(value: "SELL", child: Text("SELL")),
              ],
              onChanged: (value) {
                setState(() {
                  _type = value ?? "BUY";
                });
              },
            ),
            TextFormField(
              controller: _phpController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: "PHP Amount"),
              validator: _requiredNumber,
            ),
            TextFormField(
              controller: _btcController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: "BTC Amount"),
              validator: _requiredNumber,
            ),
            TextFormField(
              controller: _priceController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(labelText: "BTC Price"),
              validator: _requiredNumber,
            ),
            TextFormField(
              controller: _noteController,
              decoration: const InputDecoration(labelText: "Note"),
            ),
            const SizedBox(height: 30),
            ElevatedButton.icon(
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 55),
              ),
              onPressed: _saving ? null : _saveTransaction,
              icon: _saving
                  ? const SizedBox(
                      width: 18,
                      height: 18,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Icon(Icons.save),
              label: Text(_saving ? "Saving..." : "Save Transaction"),
            ),
          ],
        ),
      ),
    );
  }
}
