package com.ingegneria.app.tabfragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ingegneria.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}