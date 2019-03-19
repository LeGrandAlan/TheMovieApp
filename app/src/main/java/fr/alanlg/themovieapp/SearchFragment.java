package fr.alanlg.themovieapp;

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
import android.widget.Spinner;

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
        final EditText releaseYear = view.findViewById(R.id.releaseYear);
        final Spinner genre = view.findViewById(R.id.genre);
        final SeekBar resultNumber = view.findViewById(R.id.resultNumber);

        Button search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("keyword", keyword.getText());
                intent.putExtra("releaseYear", releaseYear.getText());
//                intent.putExtra("genre", genre.getSelectedItem().toString());
                intent.putExtra("genre", "null");
                intent.putExtra("resultNumber", resultNumber.getProgress());
                startActivity(intent);
            }
        });

    }
}
