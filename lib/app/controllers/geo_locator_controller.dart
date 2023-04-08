import 'package:get/get.dart';
import 'package:geolocator/geolocator.dart';

class GeoLocatorController extends GetxController {
  RxBool serviceEnabled=false.obs;
  Rx<LocationPermission> permission=LocationPermission.denied.obs;

  /// Determine the current position of the device.
  ///
  /// When the location services are not enabled or permissions
  /// are denied the `Future` will return an error.
  Future<Position> _determinePosition() async {


    // Test if location services are enabled.
    serviceEnabled.value = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled.value) {
      // Location services are not enabled don't continue
      // accessing the position and request users of the
      // App to enable the location services.
      return Future.error('Location services are disabled.');
    }

    permission.value = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission.value = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        // Permissions are denied, next time you could try
        // requesting permissions again (this is also where
        // Android's shouldShowRequestPermissionRationale
        // returned true. According to Android guidelines
        // your App should show an explanatory UI now.
        return Future.error('Location permissions are denied');
      }
    }

    if (permission.value == LocationPermission.deniedForever) {
      // Permissions are denied forever, handle appropriately.
      return Future.error(
          'Location permissions are permanently denied, we cannot request permissions.');
    }

    // When we reach here, permissions are granted and we can
    // continue accessing the position of the device.
    return await Geolocator.getCurrentPosition();
  }

  @override
  void onInit() {
    // TODO: implement onInit
    super.onInit();
    _determinePosition();
  }
}
