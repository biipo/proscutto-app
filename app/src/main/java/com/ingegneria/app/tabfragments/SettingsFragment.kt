package com.ingegneria.app.tabfragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ingegneria.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}