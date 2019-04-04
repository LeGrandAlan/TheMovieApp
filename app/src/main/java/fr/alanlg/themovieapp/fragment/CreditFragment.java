package fr.alanlg.themovieapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.alanlg.themovieapp.R;

public class CreditFragment extends Fragment {


    EditText keyword;
    Button search;
    CardView cardAlan;
    CardView cardEtienne;
    ImageView imageAlan;
    ImageView imageEtienne;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keyword = view.findViewById(R.id.keyword);
        search = view.findViewById(R.id.search);
        cardAlan = view.findViewById(R.id.cardViewAlan);
        cardEtienne = view.findViewById(R.id.cardViewEtienne);
        imageAlan = view.findViewById(R.id.imageViewAlan);
        imageEtienne = view.findViewById(R.id.imageViewEtienne);

        Picasso.get().load("https://media.licdn.com/dms/image/C5603AQEecNuzX-QqVg/profile-displayphoto-shrink_800_800/0?e=1559779200&v=beta&t=LBuR8ugWPLov3KNzbovCAtUBRY6MWlpZPkgBEHS6zd0").resize(350,350).into(imageAlan);
        Picasso.get().load("https://media.licdn.com/dms/image/C4E03AQF9ZGlDNPsQ_Q/profile-displayphoto-shrink_200_200/0?e=1559779200&v=beta&t=zitbcfsffmtAP-yaCF5HJW9p3ylKy3I7QEVLm6voXVc").resize(350,350).into(imageEtienne);

        cardAlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/alan-le-grand/"));
                startActivity(browserIntent);
            }
        });

        cardEtienne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/etienne-l%C3%A9crivain/"));
                startActivity(browserIntent);
            }
        });

    }
}
