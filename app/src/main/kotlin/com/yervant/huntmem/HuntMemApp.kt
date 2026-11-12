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

        // 参考 Shizuku 的标准配置：增加 FLAG_REDIRECT_STDERR，超时 10 秒
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setContext(this)
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )

        huntmemApp = this

        // 【致命 Bug 修复】
        // 原版在这里执行了：HMemServiceConnection.bind(this)
        // 绝对不能在 Application.onCreate 里调用它！
        // 此时应用尚未渲染前台窗口（Window），在后台执行 su 会直接被 Android 13 的 BAL 机制拦截，
        // 导致 Magisk 弹窗被系统抛弃，进而导致整个 rootService 连接超时死锁。
        // MainScreen.kt 本身就会在获得前台授权后再去调用 bind，这里抢跑是画蛇添足。

        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE)
    }
}
