package fr.alanlg.themovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.ResultActivity;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText keyword = view.findViewById(R.id.keyword);
        final EditText studio = view.findViewById(R.id.keywordStudio);
        final EditText releaseYear = view.findViewById(R.id.releaseYear);
        final Spinner genre = view.findViewById(R.id.genre);
        final SeekBar resultNumberMax = view.findViewById(R.id.resultNumberMax);
        final TextView textViewSeekBar = view.findViewById(R.id.textViewSeekBar);

        Button search = view.findViewById(R.id.search);

        resultNumberMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekBar.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (keyword.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Il faut des mots clés", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("keyword", keyword.getText().toString());
                intent.putExtra("studio", studio.getText().toString());
                if (releaseYear.getText().toString().isEmpty()) {
                    intent.putExtra("releaseYear", 0);
                } else {
                    intent.putExtra("releaseYear", Integer.parseInt(releaseYear.getText().toString()));
                }
//                intent.putExtra("genre", genre.getSelectedItem().toString());
                intent.putExtra("genre", "null");
                intent.putExtra("resultNumberMax", resultNumberMax.getProgress());
                startActivity(intent);
            }
        });
    }
}
