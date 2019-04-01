package fr.alanlg.themovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.ResultActivity;
import fr.alanlg.themovieapp.dao.ApiCaller;
import fr.alanlg.themovieapp.model.Compagnie;
import fr.alanlg.themovieapp.model.Genre;
import fr.alanlg.themovieapp.model.Movie;

public class SearchFragment extends Fragment {

    LinkedList<Genre> genres;
    LinkedList<Compagnie> compagnies;
    int selectedCompagnieId = -1;
    EditText keyword;
    AutoCompleteTextView studio;
    EditText releaseYear;
    Spinner genre;
    SeekBar resultNumberMax;
    TextView textViewSeekBar;
    Button search;
    ApiCaller apiCaller;
    ArrayAdapter<String> adapterAutoComplete;
    ArrayAdapter<String> adapterGenre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keyword = view.findViewById(R.id.keyword);
        studio = view.findViewById(R.id.keywordStudio);
        releaseYear = view.findViewById(R.id.releaseYear);
        genre = view.findViewById(R.id.genre);
        resultNumberMax = view.findViewById(R.id.resultNumberMax);
        textViewSeekBar = view.findViewById(R.id.textViewSeekBar);
        search = view.findViewById(R.id.search);
        apiCaller = new ApiCaller(getContext());

        adapterGenre = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, new LinkedList<String>());
        genre.setAdapter(adapterGenre);

        adapterAutoComplete = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, new LinkedList<String>());
        studio.setAdapter(adapterAutoComplete);

        apiCaller.genreList().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    Type genreType = new TypeToken<LinkedList<Genre>>() {
                    }.getType();
                    genres = new Gson().fromJson(result.get("genres"), genreType);

                    final List<String> list = new LinkedList<>();
                    list.add("");
                    genres.forEach(new Consumer<Genre>() {
                        @Override
                        public void accept(Genre genre) {
                            list.add(genre.name());
                        }
                    });
                    adapterGenre.clear();
                    adapterGenre.addAll(list);
                    adapterGenre.notifyDataSetChanged();
                }
            }
        });


        studio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (studio.getText().length() >= 3) {
                    Log.d("dsffddsdfsfd", "afterTextChanged: " + s.toString());
                    apiCaller.searchCompagnie(studio.getText().toString()).setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                Type compagnieType = new TypeToken<LinkedList<Compagnie>>() {
                                }.getType();
                                compagnies = new Gson().fromJson(result.get("results"), compagnieType);

                                final List<String> list = new LinkedList<>();
                                compagnies.forEach(new Consumer<Compagnie>() {
                                    @Override
                                    public void accept(Compagnie compagnie) {
                                        list.add(compagnie.name());
                                    }
                                });

                                adapterAutoComplete.clear();
                                adapterAutoComplete.addAll(list);
                                adapterAutoComplete.notifyDataSetChanged();
                            }
                        }
                    });
                }

            }
        });

        studio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCompagnieId = compagnies.get(position).getId();
            }
        });

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
                intent.putExtra("studio", selectedCompagnieId == -1 ? "" : String.valueOf(selectedCompagnieId));
                if (releaseYear.getText().toString().isEmpty()) {
                    intent.putExtra("releaseYear", 0);
                } else {
                    intent.putExtra("releaseYear", Integer.parseInt(releaseYear.getText().toString()));
                }
                intent.putExtra("genre", genre.getSelectedItemPosition() == 0 ? "" : String.valueOf(genres.get(genre.getSelectedItemPosition() - 1).getId()));
                intent.putExtra("resultNumberMax", resultNumberMax.getProgress()*20);
                startActivity(intent);
            }
        });
    }
}
