package com.makkhay.androiddictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.makkhay.androiddictionary.database.DbBackend;
import com.makkhay.androiddictionary.database.QuizObject;
import com.makkhay.androiddictionary.database.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    // List of all dictionary words
    private List<QuizObject> dictionaryWords;
    private List<QuizObject> filteredList;

    // RecycleView adapter object
    private SimpleItemRecyclerViewAdapter mAdapter;
    int var_visibility = View.VISIBLE;
    int var_visibility1 = View.GONE;
    // Search edit box
    private EditText searchBox;

    // Database object
    private DbBackend dbBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Google Analytic tracking code.
        Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName(this.getClass().getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

        // Instantiate database object and call return dictionary method
        dbBackend = new DbBackend(ItemListActivity.this);
        dictionaryWords = dbBackend.dictionaryWords();

        filteredList = new ArrayList<QuizObject>();
        filteredList.addAll(dictionaryWords);

        searchBox = (EditText)findViewById(R.id.search_box);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.item_list);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        assert recyclerView != null;
        mAdapter = new SimpleItemRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);

        // search suggestions using the edittext widget
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Alphabet " + s.toString());
                mAdapter.getFilter().filter(s.toString());
                System.out.println("Alphabet Count " + mAdapter.mValues.size());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    // create a custom RecycleViewAdapter class
    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {

        private List<QuizObject> mValues;
        private CustomFilter mFilter;

        public SimpleItemRecyclerViewAdapter(List<QuizObject> items) {
            mValues = items;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
            view.setVisibility(var_visibility);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getWord());

            final String valueOfId = String.valueOf(holder.mItem.getId());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, valueOfId);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, valueOfId);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public QuizObject mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

        public class CustomFilter extends Filter {

            private SimpleItemRecyclerViewAdapter mAdapter;

            private CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();

                if (constraint.length() == 0) {
                    filteredList.addAll(dictionaryWords);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();

                    for (final QuizObject mWords : dictionaryWords) {
                        if (mWords.getWord().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

               // filteredList.clear();
                //filteredList.addAll((List<QuizObject>) results.values);
                System.out.println("Count Number 2 " + ((List<QuizObject>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }


}
