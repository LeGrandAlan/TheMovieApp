package fr.alanlg.themovieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.alanlg.themovieapp.R;
import fr.alanlg.themovieapp.model.CastMember;
import fr.alanlg.themovieapp.model.CrewMember;

public class MemberCardGalleryAdapter extends PagerAdapter {

    private List<Member> members;
    private LayoutInflater layoutInflater;
    private Context context;

    public MemberCardGalleryAdapter(List<Member> castMembers, Context context) {
        this.members = castMembers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_gallery_item, container, false);

        ImageView cardGalleryImage = view.findViewById(R.id.cardGalleryImage);
        TextView cardGalleryText = view.findViewById(R.id.cardGalleryText);
        TextView cardGalleryText2 = view.findViewById(R.id.cardGalleryText2);

        if (members.get(position).getClass() == CastMember.class) {
            CastMember castMember = (CastMember) members.get(position);

            Picasso.get().load(castMember.getProfileImagePath()).noFade().placeholder(R.drawable.image_loading).into(cardGalleryImage);
            cardGalleryText.setText("Nom : " + castMember.getName());
            cardGalleryText2.setText("Rôle joué : " + castMember.getCharacter());
        } else if (members.get(position).getClass() == CrewMember.class){
            CrewMember crewMember = (CrewMember) members.get(position);

            Picasso.get().load(crewMember.getProfileImagePath()).noFade().placeholder(R.drawable.image_loading).into(cardGalleryImage);
            cardGalleryText.setText("Nom : " + crewMember.getName());
            cardGalleryText2.setText("Poste : " + crewMember.getJob());
        }

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public float getPageWidth(int position) {
        return(0.5f);
    }

}
