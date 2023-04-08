import 'package:animated_bottom_navigation_bar/animated_bottom_navigation_bar.dart';
import 'package:child_safe/app/controllers/AppColors.dart';
import 'package:child_safe/app/routes/app_pages.dart';
import 'package:device_apps/device_apps.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controllers/home_controller.dart';
import 'package:timeago/timeago.dart' as timeago;

class HomeView extends GetView<HomeController> {
  List l1=[{}];


  HomeView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      // floatingActionButton: FloatingActionButton(onPressed: () {  },
      //   //params
      // ),


      // floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      bottomNavigationBar: Obx(()=>AnimatedBottomNavigationBar(
        activeColor: Colors.blueAccent,
          leftCornerRadius: 32,
          rightCornerRadius: 32,
          gapLocation: GapLocation.none,
          notchSmoothness: NotchSmoothness.defaultEdge,

          icons: [Icons.home,Icons.auto_graph,Icons.search,Icons.person],
          activeIndex: controller.bottomIndex.value,
          onTap: (index){
            controller.bottomIndex.value=index;
            controller.bottomIndex.refresh();
          }),
          //other params
        ),

      appBar: AppBar(
        title: const Text('HomeView'),
        centerTitle: true,

      ),
      body:  Container(
        height: Get.height,
        child: SingleChildScrollView(
          child: Container(
            height: Get.height,
            child: Column(
              children: [
                Card(
                  child: Container(
                    height: Get.height/4,
                  ),
                ),
                Container(
                  child: Expanded(
                    child: Obx(()=>ListView.builder(
                          itemCount: controller.infos.length,
                          itemBuilder: (c,i){
                            
                            return  Container(
                                child: FutureBuilder(
                                  future: controller.getApplication(controller.infos[i].packageName),

                                  builder: (BuildContext context, AsyncSnapshot<ApplicationWithIcon?> snapshot) {
                                    if(snapshot.data==null || snapshot.data!.icon==null){
                                      return Container(
                                        child: ListTile(
                                          title: Text(controller.infos[i].appName),
                                          leading: Icon(Icons.image),
                                          trailing: Text("${controller.infos[i].usage.inMinutes} min"),
                                        ),
                                      );

                                    }
                                    return ListTile(
                                      title: Text(snapshot.data!.appName),
                                      leading: Image.memory(snapshot.data!.icon),
                                      subtitle: Text("Last Used: ${timeago.format(controller.infos[i].endDate)}"),
                                      trailing: Column(

                                        crossAxisAlignment: CrossAxisAlignment.center,
                                        mainAxisAlignment: MainAxisAlignment.center,
                                        children: [
                                          Text("${controller.infos[i].usage.inMinutes} m",style: TextStyle(fontWeight: FontWeight.w600),),

                                        ],
                                      ),
                                    );

                                },

                                ),


                            );
                          }),
                    ),
                  ),
                )
              ],
            ),
          )
        ),
      ),

    );
  }
}
