import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:get/get.dart';

import 'app/routes/app_pages.dart';
import 'firebase_options.dart';



const MethodChannel platform = const MethodChannel('com.example/helper');
void main() async{
  WidgetsFlutterBinding.ensureInitialized();

  String op="";
  try{
    op=await platform.invokeMethod("checkAccess");
  }catch(e){
    op=e.toString();
  }
  print("Final Op:$op");
  runApp(
    GetMaterialApp(
      title: "Application",
      initialRoute: AppPages.INITIAL,
      getPages: AppPages.routes,
    ),
  );
}
