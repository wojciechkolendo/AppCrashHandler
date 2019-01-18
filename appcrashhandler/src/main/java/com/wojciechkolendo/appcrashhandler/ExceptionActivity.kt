package com.wojciechkolendo.appcrashhandler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Log
import androidx.appcompat.app.AlertDialog

/**
 * @author Wojtek Kolendo
 */
class ExceptionActivity : Activity() {

    private val NEW_LINE = "\n"
    private val SPACE = " "
    private lateinit var DEVICE: String
    private lateinit var APPLICATION: String
    private lateinit var EXCEPTION: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val throwable = intent.getSerializableExtra(EXTRA_EXCEPTION) as Throwable

        DEVICE = getString(R.string.app_crash_handler_device)
        APPLICATION = getString(R.string.app_crash_handler_application)
        EXCEPTION = getString(R.string.app_crash_handler_exception)

        val builder = SpannableStringBuilder()
        var start = 0
        appendDevice(builder)
        builder.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, start + DEVICE.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(NEW_LINE).append(NEW_LINE)
        start = builder.length
        appendApplication(builder)
        builder.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, start + APPLICATION.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(NEW_LINE).append(NEW_LINE)
        start = builder.length
        appendException(throwable, builder)
        builder.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, start + EXCEPTION.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val message = builder.toString()

        AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setTitle(getString(R.string.app_crash_handler_title, getApplicationName(applicationContext)))
                .setMessage(builder)
                .setPositiveButton(R.string.app_crash_handler_send_report) { _, _ ->
                    startActivity(Intent().apply {
                        type = "plain/text"
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(Intent.EXTRA_TEXT, "/code $message")
                    })
                    finish()
                }
                .setNeutralButton(R.string.app_crash_handler_close) { _, _ -> finishAffinity() }
                .show()
    }

    private fun appendDevice(builder: SpannableStringBuilder) {
        builder.append(DEVICE).append(NEW_LINE)
        if (!TextUtils.isEmpty(Build.BRAND)) {
            builder.append(Build.BRAND).append(SPACE)
        }
        if (!TextUtils.isEmpty(Build.MANUFACTURER)) {
            builder.append(Build.MANUFACTURER).append(SPACE)
        }
        if (!TextUtils.isEmpty(Build.MODEL)) {
            builder.append(Build.MODEL).append(SPACE)
        }
        if (!TextUtils.isEmpty(Build.DEVICE)) {
            builder.append('(').append(Build.DEVICE).append(')').append(SPACE)
        }
        builder.append(NEW_LINE)
                .append("Android").append(Build.VERSION.RELEASE)
                .append(" (Api ").append(Build.VERSION.SDK_INT.toString()).append(")")
    }

    private fun appendApplication(builder: SpannableStringBuilder) {
        builder.append(APPLICATION).append(NEW_LINE)
                .append(getApplicationName(applicationContext)).append(SPACE)
                .append(getApplicationVersion(applicationContext))
    }

    private fun appendException(throwable: Throwable, builder: SpannableStringBuilder) {
        builder.append(EXCEPTION).append(NEW_LINE)
        builder.append(Log.getStackTraceString(throwable))
    }

    private fun getApplicationName(context: Context): String {
        return context.getString(context.applicationInfo.labelRes)
    }

    private fun getApplicationVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
            "v" + packageInfo.versionName + "(" + packageInfo.versionCode.toString() + ")"
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
}