package `fun`.gladkikh.fastpallet5.repository

import `fun`.gladkikh.fastpallet5.domain.intety.SettingsPref
import android.content.Context
import androidx.preference.PreferenceManager

class SettingsRepository {
    var settings = SettingsPref()
       private set

    fun readSettings(context: Context) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        val host = "preference_host"
        val login = "preference_login"
        val pass = "preference_pass"
        val code = "preference_code_1c"
        val listTsd = "list_tsd"



        settings = SettingsPref(
            host = sharedPref.getString(host, "http://172.31.255.150/rmmt/hs/api/"),
            code = sharedPref.getString(code, "333"),
            login = sharedPref.getString(login, "Администратор"),
            pass = sharedPref.getString(pass, ""),
            typeTsd = sharedPref.getString(listTsd, 1.toString()).toIntOrNull()
        )
    }
}