package com.wojciechkolendo.appcrashhandler

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import software.rsquared.androidlogger.Logger
import java.lang.Exception

/**
 * Uncaught exception handler. This handler is invoked in case any Thread dies due to an unhandled exception.
 *
 * @author Wojtek Kolendo
 */
const val EXTRA_EXCEPTION = "extra_exception"

@SuppressLint("PrivateApi")
class AppCrashHandler : Thread.UncaughtExceptionHandler {

    private val context: Application by lazy {
        try {
            (Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null)) as Application
        } catch (e: Exception) {
            Logger.error(e)
            Application()
        }
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        context.startActivity(Intent(context, ExceptionActivity::class.java).apply {
            putExtra(EXTRA_EXCEPTION, throwable)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK);
        })
    }

    companion object {
        @JvmStatic
        fun setAsDefault() {
            Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler())
        }
    }
}