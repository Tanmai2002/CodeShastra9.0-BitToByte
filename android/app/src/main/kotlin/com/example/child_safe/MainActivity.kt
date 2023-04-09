package com.example.child_safe

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import com.example.child_safe.Util.isAccessibilityServiceEnabled
import com.example.child_safe.Util.prepareBadKeywords
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {

    private val CHANNEL = "com.example/helper"


//
    @ExperimentalStdlibApi
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine);
        prepareBadKeywords(this)



//        button_enable_acc = findViewById<View>(R.id.button_enable_acc) as SwitchButton
//        if (isAccessibilityServiceEnabled(this)) {
////            button_enable_acc.setChecked(true)
//            Log.d("Enable",
//                "Accessiblity Enabled"
//            )
//
//        }

        Log.d("Enable",
            "Accessiblity Not Enabled"
        )
////        button_enable_acc.setOnCheckedChangeListener(object : OnCheckedChangeListener() {
////            fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
////                if (isChecked) {
////
////                }
////            }
////        })
//
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler{
                call, result ->
            when {

                call.method.equals("checkAccess") -> {
                    if (Util.isAccessibilityServiceEnabled(this)
                    ) {


        result.success("Enabled")
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        startActivityForResult(intent, 0)

                    }else{

        result.success("Disabled")
                    }

//                    changeColor(result)
                }
            }
        }
    }
}
