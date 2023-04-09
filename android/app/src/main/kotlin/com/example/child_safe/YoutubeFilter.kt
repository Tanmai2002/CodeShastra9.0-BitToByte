package com.example.child_safe

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.util.Log


import android.app.PendingIntent

import android.content.Intent

import android.net.Uri

import android.view.accessibility.AccessibilityNodeInfo
import java.util.*


@TargetApi(Build.VERSION_CODES.DONUT)
class YoutubeFilter : AccessibilityService() {
    private val YOUTUBE_FULL_SCREEN_BTN_ID = "com.google.android.youtube:id/fullscreen_button"
    private val YOUTUBE_VIDEO_TITLE_ID = "com.google.android.youtube:id/title"
    private val TAG = "YouTubeFilterService"
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        try {
            Log.d(TAG, "Event Name : " + AccessibilityEvent.eventTypeToString(event!!.eventType))
            if (rootInActiveWindow.findAccessibilityNodeInfosByViewId(YOUTUBE_FULL_SCREEN_BTN_ID) != null
                && rootInActiveWindow.findAccessibilityNodeInfosByViewId(YOUTUBE_FULL_SCREEN_BTN_ID).size > 0
            ) {
                val listOfResIDs =
                    rootInActiveWindow.findAccessibilityNodeInfosByViewId(YOUTUBE_VIDEO_TITLE_ID)
                Log.d(
                    TAG,
                    "[MyAccessibilityService] inside onAccessibilityEvent() list size : " + listOfResIDs!!.size
                )
                if (listOfResIDs != null && listOfResIDs.size > 0 && listOfResIDs[0] != null && listOfResIDs[0]!!.text != null && listOfResIDs[0]!!.text.length > 0) {
                    listOfResIDs[0]!!.text.toString().split("\\[\\s]+".toRegex()).toTypedArray()
                    val splittedTitle: Array<String>? =( Util.splitParts(
                        listOfResIDs[0]!!.text.toString().lowercase(Locale.getDefault())
                    ))
                    val ngramTitle: List<String> = Util.generateNgramsUpto(
                        listOfResIDs[0]!!.text.toString().lowercase(Locale.getDefault()),
                        splittedTitle?.size?:0
                    )
                    if (ngramTitle != null && ngramTitle.size > 0) {
                        for (i in ngramTitle.indices) {
                            Util.prepareBadKeywords(this)
                            if (Util.ADULT_KEYWORDMAP.containsKey(ngramTitle[i])) {
                                val video_path: String? = Util.ADULT_KEYWORDMAP.get(ngramTitle[i])
                                val uri = Uri.parse(video_path)
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                intent.addFlags(PendingIntent.FLAG_CANCEL_CURRENT)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "[MyAccessibilityService] inside onAccessibilityEvent() Exception is :$e")
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }
}