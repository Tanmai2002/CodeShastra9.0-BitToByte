import 'package:child_safe/app/controllers/notification_controller.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

import '../../../../firebase_options.dart';
import '../../../routes/app_pages.dart';
@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  // If you're going to use other Firebase services in the background, such as Firestore,
  // make sure you call `initializeApp` before using other Firebase services.
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  print("Handling a background message: ${message.messageId}");
}
class SplashScreenController extends GetxController {
  //TODO: Implement SplashScreenController

  void initializeFirebase()async{
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );

    // MethodChannel platform = const MethodChannel('flutter.native/helper');
    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);


    Get.put(NotificationController(),permanent: true);



  }
  final count = 0.obs;
  @override
  void onInit() {
    initializeFirebase();
    Future.delayed(5.seconds,(){
      print("Running");

      Get.toNamed(Routes.HOME);

    });
    super.onInit();
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  void increment() => count.value++;
}
