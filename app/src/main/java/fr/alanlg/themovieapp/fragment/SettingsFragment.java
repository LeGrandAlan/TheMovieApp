package fr.alanlg.themovieapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Objects;

import fr.alanlg.themovieapp.R;

public class SettingsFragment extends Fragment {

    Switch aSwitch;
    SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aSwitch = view.findViewById(R.id.switch2);
        mPrefs = Objects.requireNonNull(getContext()).getSharedPreferences("prefs", 0);
        aSwitch.setChecked(Boolean.parseBoolean(mPrefs.getString("adultMovies", "false")));


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("adultMovies", String.valueOf(isChecked)).apply();
            }
        });

    }
}
