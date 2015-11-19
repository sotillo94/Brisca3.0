package com.brisca.moviles.uva.brisca30;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class FragmentoPreferencias extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);


    }
}
