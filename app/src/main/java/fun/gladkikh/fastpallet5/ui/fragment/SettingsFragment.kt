package `fun`.gladkikh.fastpallet5.ui.fragment

import `fun`.gladkikh.fastpallet5.R
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onPreferenceChange(p0: Preference?, p1: Any?): Boolean {
        Toast.makeText(
            activity,
            "Будь внимателен!",
            Toast.LENGTH_SHORT
        ).show()

        return true
    }


    override fun onCreatePreferences(saveInstantState: Bundle?, rootkey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootkey)
        findPreference<EditTextPreference>("preference_host")!!.onPreferenceChangeListener = this

    }

}