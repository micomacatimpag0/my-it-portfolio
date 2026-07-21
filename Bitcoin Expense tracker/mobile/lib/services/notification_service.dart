import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_messaging/firebase_messaging.dart';


class NotificationService {


  final DatabaseReference db =
      FirebaseDatabase.instance.ref();



  Future<void> saveToken() async {


    final token =
        await FirebaseMessaging.instance.getToken();



    if(token == null){
      return;
    }



    await db
        .child("users/testUser/fcmToken")
        .set(token);



    print("FCM token saved");

  }

}