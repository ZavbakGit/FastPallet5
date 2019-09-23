package `fun`.gladkikh.fastpallet5

import android.annotation.SuppressLint
import android.provider.Settings

@SuppressLint("HardwareIds")
object Constants {
    const val BASE_URL = "http://172.31.255.168/rmmt/hs/api/"
    const val APP_VERSION = BuildConfig.VERSION_NAME
    val UID by lazy {
        App.appContext()?.let {
            Settings.Secure.getString(it.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
    val OS_VERSION by lazy { android.os.Build.VERSION.SDK_INT.toString() }

    val KEY_MENU = 12 // 5
    val KEY_DELL = 16 // 9
    val KEY_LOAD = 11 // 4
    val KEY_ADD = 8 // 1
    val KEY_TEST = 7 // 0
}

