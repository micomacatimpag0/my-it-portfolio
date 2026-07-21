import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';

import '../services/alert_service.dart';
import '../utils/formatter.dart';

class AlertsScreen extends StatefulWidget {
  const AlertsScreen({super.key});

  @override
  State<AlertsScreen> createState() => _AlertsScreenState();
}

class _AlertsScreenState extends State<AlertsScreen> {
  final AlertService _alertService = AlertService();
  final TextEditingController _targetController = TextEditingController();

  bool _enabled = false;
  bool _saving = false;

  @override
  void dispose() {
    _targetController.dispose();
    super.dispose();
  }

  Future<void> _saveAlert() async {
    final targetPrice = double.tryParse(_targetController.text);

    if (targetPrice == null || targetPrice <= 0) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Enter a valid target price")),
      );
      return;
    }

    setState(() {
      _saving = true;
    });

    await _alertService.saveAlert(
      targetPrice: targetPrice,
      enabled: _enabled,
    );

    if (!mounted) {
      return;
    }

    setState(() {
      _saving = false;
    });
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text("Alert saved")),
    );
  }

  void _loadAlert(Map<dynamic, dynamic> data) {
    final targetPrice = AppFormatter.parseDouble(data["targetPrice"]);
    final enabled = data["enabled"] == true;

    if (_targetController.text.isEmpty && targetPrice > 0) {
      _targetController.text = targetPrice.toStringAsFixed(2);
    }

    if (_enabled != enabled) {
      _enabled = enabled;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Alert Settings"),
      ),
      body: StreamBuilder<DatabaseEvent>(
        stream: _alertService.getAlert(),
        builder: (context, snapshot) {
          final data = snapshot.data?.snapshot.value as Map<dynamic, dynamic>?;

          if (data != null) {
            _loadAlert(data);
          }

          return ListView(
            padding: const EdgeInsets.all(20),
            children: [
              TextField(
                controller: _targetController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: "Target BTC Price",
                  prefixText: "₱",
                ),
              ),
              const SizedBox(height: 16),
              SwitchListTile(
                contentPadding: EdgeInsets.zero,
                title: const Text("Enable Alert"),
                subtitle: const Text("Trigger when BTC price reaches target"),
                value: _enabled,
                onChanged: (value) {
                  setState(() {
                    _enabled = value;
                  });
                },
              ),
              const SizedBox(height: 24),
              ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  minimumSize: const Size(double.infinity, 55),
                ),
                onPressed: _saving ? null : _saveAlert,
                icon: const Icon(Icons.notifications_active),
                label: Text(_saving ? "Saving..." : "Save Alert"),
              ),
            ],
          );
        },
      ),
    );
  }
}
