/*
 * HuntMemory - Process Memory Editor & Scanner for Android
 * Copyright (C) 2026 Yervant7
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.yervant.huntmem

import android.app.Application
import android.content.SharedPreferences
import com.topjohnwu.superuser.Shell

lateinit var huntmemApp: HuntMemApplication

class HuntMemApplication : Application() {

    companion object {
        const val SP_NAME = "hconfig"
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        Shell.enableVerboseLogging = BuildConfig.DEBUG

        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setContext(this)
                .setTimeout(10)
        )

        huntmemApp = this

        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE)
    }
}
