package com.example.child_safe

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


object Util {
    var ADULT_KEYWORDMAP: MutableMap<String, String> = HashMap()
    private var reader: BufferedReader? = null
    private var `in`: InputStream? = null
    private const val TAG = "YouTubeFilterService"
    val ADULT_KEYWORD_FILE_BY_NAME = mutableMapOf<String,Int>();


    init {
        val valuesByName: MutableMap<String, Int> = HashMap()
        valuesByName["PORN_0"] =R.raw.porn_0
        valuesByName["PORN_1"] = R.raw.porn_1
        valuesByName["PORN_2"] = R.raw.porn_2
        valuesByName["PORN_3"] = R.raw.porn_start_list
        ADULT_KEYWORD_FILE_BY_NAME.putAll( Collections.unmodifiableMap(valuesByName));
    }

    private const val whiteSpaceRegex = "[\\s]+"
    fun prepareBadKeywords(appContext: Context) {
        try {

            if (ADULT_KEYWORDMAP.size != 0) {
                return
            }
            ADULT_KEYWORDMAP["porn"] =
                "https://www.youtube.com/watch?v=xERG4h3JWis"
            for (i in 0 until ADULT_KEYWORD_FILE_BY_NAME.size) {
                `in` = appContext.resources.openRawResource(ADULT_KEYWORD_FILE_BY_NAME["PORN_$i"]!!)
                reader = BufferedReader(InputStreamReader(`in`))
                var line: String? = null
                while (reader!!.readLine().also { line = it } != null) {
                    if (line!!.isEmpty()) {
                        continue
                    }
                    line = line!!.trim { it <= ' ' }
                    if (line!!.startsWith("#")) {
                        continue
                    }
                    //Using for redirect to specific video
                    /* String[] parts = line.split(" ");
                    if (parts.length == 2
                            && !"localhost".equalsIgnoreCase(parts[1])) {
                        ADULT_KEYWORDMAP.put(parts[1], parts[0]);
                    }*/if (line!!.startsWith("@")) {
                        ADULT_KEYWORDMAP[line!!.substring(1)] =
                            "https://www.youtube.com/watch?v=xERG4h3JWis"
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "[Util] inside prepareBadKeywords() Exception : $e")
        } finally {
            try {
                reader?.close()
                `in`?.close()
            } catch (ex: IOException) {
            }
        }
    }

    fun ngrams(str: String, n: Int): List<String> {
        val ngrams: MutableList<String> = ArrayList()
        val words = str.split(" ".toRegex()).toTypedArray()
        for (i in 0 until words.size - n + 1) ngrams.add(concat(words, i, i + n))
        return ngrams
    }

    private fun concat(words: Array<String>, start: Int, end: Int): String {
        val sb = StringBuilder()
        for (i in start until end) sb.append((if (i > start) " " else "") + words[i])
        return sb.toString()
    }

    /**
     *
     * @param str should has at least one string
     * @param maxGramSize should be 1 at least
     * @return set of continuous word n-grams up to maxGramSize from the sentence
     */
    fun generateNgramsUpto(str: String, maxGramSize: Int): List<String> {
        val sentence = Arrays.asList(*str.split("[\\s]+".toRegex()).toTypedArray())
        val ngrams: MutableList<String> = ArrayList()
        var ngramSize = 0
        var sb: StringBuilder? = null

        //sentence becomes ngrams
        val it: ListIterator<String> = sentence.listIterator()
        while (it.hasNext()) {
            val word = it.next()

            //1- add the word itself
            sb = StringBuilder(word)
            ngrams.add(word)
            ngramSize = 1
            it.previous()

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                sb.insert(0, ' ')
                sb.insert(0, it.previous())
                ngrams.add(sb.toString())
                ngramSize++
            }

            //go back to initial position
            while (ngramSize > 0) {
                ngramSize--
                it.next()
            }
        }
        return ngrams
    }

    fun splitParts(title: String?): Array<String>? {
        return try {
            if (title == null || title.length < 1) {
                return null
            }
            title.split(whiteSpaceRegex.toRegex()).toTypedArray()
        } catch (e: Exception) {
            Log.d(TAG, "[Util] inside splitParts() Exception is : $e")
            null
        }
    }

    @SuppressLint("NewApi")
    fun isAccessibilityServiceEnabled(appContext: Context): Boolean {
        var isEnabled = true
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val accessibilityManager =
                    appContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
                if (accessibilityManager.isEnabled) {
                    val list = accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
                    )
                    if (list != null) {
                        if (list.size != 0) {
                            Log.d(
                                TAG,
                                "AccessibilityManager AccessibilityServiceInfo package name to found " + appContext.packageName
                            )
                            for (service in list) {
                                if (service.id.contains(appContext.packageName)) {
                                    Log.d(
                                        TAG,
                                        "AccessibilityManager AccessibilityServiceInfo service found " + service.id
                                    )
                                    isEnabled = true
                                    break
                                } else {
                                    Log.d(
                                        TAG,
                                        "[Util]AccessibilityManager AccessibilityServiceInfo not required service  " + service.id
                                    )
                                    isEnabled = false
                                }
                            }
                        } else {
                            Log.d(TAG, "AccessibilityManager nothing in list ")
                            isEnabled = false
                        }
                    } else {
                        isEnabled = false
                    }
                } else {
                    Log.d(TAG, "Util :AccessibilityManager  is not enabled")
                    isEnabled = false
                }
            } else {
                Log.d(
                    TAG,
                    "Util :AccessibilityManager Build version is less than ICE_CREAM_SANDWICH"
                )
            }
            Log.d(TAG, "Util :AccessibilityManager  returning status$isEnabled")
            isEnabled
        } catch (ex: Exception) {
            Log.d(TAG, "[Util]:Exception occuered in  isAccessibilityServiceEnabled returning true")
            true.also { isEnabled = it }
        }
    }
}