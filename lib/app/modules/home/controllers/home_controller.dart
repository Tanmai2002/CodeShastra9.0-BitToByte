import 'package:app_usage/app_usage.dart';
import 'package:device_apps/device_apps.dart';
import 'package:get/get.dart';

class HomeController extends GetxController {
  RxInt bottomIndex=0.obs;
  RxList<AppUsageInfo> infos=<AppUsageInfo>[].obs;


  Future<ApplicationWithIcon?> getApplication(String Package_Name)async{

    ApplicationWithIcon? app = (await DeviceApps.getApp(Package_Name,true)) as ApplicationWithIcon;
    return app;
  }
  void getUsageStats() async {
    try {
      DateTime endDate = DateTime.now();
      DateTime startDate = endDate.subtract(Duration(hours: 1));
      List<AppUsageInfo> infoList =
      await AppUsage().getAppUsage(startDate, endDate);
      infos.value = infoList;
      infos.refresh();

      for (var info in infoList) {
        print(info.toString());
      }
    } on AppUsageException catch (exception) {
      print(exception);
    }

  }
  final count = 0.obs;
  @override
  void onInit() {
    getUsageStats();
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
