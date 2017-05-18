package com.makkhay.androiddictionary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makkhay.androiddictionary.database.DbBackend;
import com.makkhay.androiddictionary.database.QuizObject;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /* Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView dictionaryWord = (TextView)rootView.findViewById(R.id.word);
        TextView dictionaryMeaning = (TextView)rootView.findViewById(R.id.meaning);
        int dataSourceId = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));

        DbBackend dbBackend = new DbBackend(getActivity());
        QuizObject dictionaryObject = dbBackend.getQuizById(dataSourceId);

        if(null != dictionaryObject){
            dictionaryWord.setText(dictionaryObject.getWord());
            dictionaryMeaning.setText(dictionaryObject.getMeaning());
        }

        return rootView;
    }
}
