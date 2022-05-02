package com.example.finalproject;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import com.example.finalproject.R;

public class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final SwitchPreference Switch = (SwitchPreference) findPreference("background_music");

        //Log.d("on","on");
        if (Switch != null) {

            Log.d("on","on");
            Switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if((boolean)newValue){
                        testFirebase.mp.start();
                        Log.d("on","on");
                    }
                    else {
                        testFirebase.mp.pause();
                        Log.d("off","off");
                    }
                    return true;
                }
            });
        }
    }




}