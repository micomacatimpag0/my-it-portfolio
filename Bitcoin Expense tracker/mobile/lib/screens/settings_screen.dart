import 'package:flutter/material.dart';

import '../utils/constants.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Settings"),
      ),
      body: ListView(
        padding: const EdgeInsets.all(20),
        children: [
          const Card(
            child: ListTile(
              leading: Icon(Icons.person),
              title: Text("Active User"),
              subtitle: Text(AppConstants.userId),
            ),
          ),
          Card(
            child: ListTile(
              leading: Icon(Icons.storage),
              title: Text("Transactions Path"),
              subtitle: Text(AppConstants.transactionsFullPath),
            ),
          ),
          Card(
            child: ListTile(
              leading: Icon(Icons.notifications),
              title: Text("Alert Path"),
              subtitle: Text(AppConstants.btcAlertFullPath),
            ),
          ),
        ],
      ),
    );
  }
}
