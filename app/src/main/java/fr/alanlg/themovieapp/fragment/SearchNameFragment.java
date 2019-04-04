package fr.alanlg.themovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.ResultActivity;
import fr.alanlg.themovieapp.dao.ApiCaller;

public class SearchNameFragment extends Fragment {


    EditText keyword;
    Button search;
    SeekBar resultNumberMax;
    TextView textViewSeekBar;

    ApiCaller apiCaller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keyword = view.findViewById(R.id.keyword);
        search = view.findViewById(R.id.search);
        apiCaller = new ApiCaller(Objects.requireNonNull(getContext()));
        resultNumberMax = view.findViewById(R.id.resultNumberMax);
        textViewSeekBar = view.findViewById(R.id.textViewSeekBar);


        resultNumberMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekBar.setText(String.valueOf(progress * 20));
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
                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("keyword", keyword.getText().toString());

                intent.putExtra("resultNumberMax", resultNumberMax.getProgress() * 20);
                startActivity(intent);
            }
        });
    }
}
