package fr.alanlg.themovieapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.alanlg.themovieapp.R;

public class FilterDialogFragment extends DialogFragment {
    public static FilterDialogFragment newInstance(int title) {
        FilterDialogFragment dialog = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_filter, container, false);

//        Button button = (Button) v.findViewById(R.id.buttonShow);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                ((DialogActivity) getActivity())
//                        .showDialogType(DialogActivity.TYPE_ALERT_DIALOG);
//            }
//        });

        getDialog().setTitle(getArguments().getInt("title"));

        return v;
    }
}
