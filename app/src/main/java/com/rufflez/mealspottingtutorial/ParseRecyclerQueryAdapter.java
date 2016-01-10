package com.rufflez.mealspottingtutorial;

/**
 * Created by rufflez on 1/8/16.
 */
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.QueryFactory;

import java.util.ArrayList;
import java.util.List;


/**
 *  NEARLY IDENTICAL REPLACEMENT FOR ParseQueryAdapter ON ListView.
 *  REQUIRES THAT YOU SUBCLASS TO CREATE ViewHolder, onBindViewHolder(), and onCreateViewHolder
 *  AS ENFORCED BY THE RECYCLERVIEW PATTERN.
 *
 *  TESTED SUCCESSFULLY with RecyclerView v7:21.0.3
 *  AND with SuperRecyclerView by Malinskiy
 *  @ https://github.com/Malinskiy/SuperRecyclerView
 *  SHOULD WORK WITH UltimateRecyclerView
 */
public abstract class ParseRecyclerQueryAdapter<T extends ParseObject, U extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<U>
{

    private final QueryFactory<T> mFactory;
    private final boolean hasStableIds;
    private final List<T> mItems;

    // PRIMARY CONSTRUCTOR
    public ParseRecyclerQueryAdapter(final QueryFactory<T> factory, final boolean hasStableIds) {
        mFactory = factory;
        mItems = new ArrayList<T>();
        mDataSetListeners = new ArrayList<OnDataSetChangedListener>();
        mQueryListeners = new ArrayList<OnQueryLoadListener<T>>();
        this.hasStableIds = hasStableIds;

        setHasStableIds(hasStableIds);
        loadObjects();
    }

    // ALTERNATE CONSTRUCTOR
    public ParseRecyclerQueryAdapter(final String className, final boolean hasStableIds) {
        this(new QueryFactory<T>() {

            @Override public ParseQuery<T> create() {
                return ParseQuery.getQuery(className);
            }
        }, hasStableIds);
    }

    // ALTERNATE CONSTRUCTOR
    public ParseRecyclerQueryAdapter(final Class<T> clazz, final boolean hasStableIds) {
        this(new QueryFactory<T>() {

            @Override public ParseQuery<T> create() {
                return ParseQuery.getQuery(clazz);
            }
        }, hasStableIds);
    }


  /*
   *  REQUIRED RECYCLERVIEW METHOD OVERRIDES
   */

    @Override
    public long getItemId(int position) {
        if (hasStableIds) {
            return position;
        }
        return super.getItemId(position);
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    public T getItem(int position) { return mItems.get(position); }

    public List<T> getItems() { return mItems; }





    /**
     * Apply alterations to query prior to running findInBackground.
     */
    protected void onFilterQuery(ParseQuery<T> query) {
        // provide override for filtering query
    }

    public void loadObjects() {
        dispatchOnLoading();
        final ParseQuery<T> query = mFactory.create();
        onFilterQuery(query);
        query.findInBackground(new FindCallback<T>() {;

            @Override public void done(
                    List<T> queriedItems,
                    @Nullable ParseException e) {
                if (e == null) {
                    mItems.clear();
                    mItems.addAll(queriedItems);
                    dispatchOnLoaded(queriedItems, e);
                    notifyDataSetChanged();
                    fireOnDataSetChanged();
                }
            }
        });
    }



    public interface OnDataSetChangedListener {
        public void onDataSetChanged();
    }

    private final List<OnDataSetChangedListener> mDataSetListeners;

    public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
        mDataSetListeners.add(listener);
    }

    public void removeOnDataSetChangedListener(OnDataSetChangedListener listener) {
        if (mDataSetListeners.contains(listener)) {
            mDataSetListeners.remove(listener);
        }
    }

    protected void fireOnDataSetChanged() {
        for (int i = 0; i < mDataSetListeners.size(); i++) {
            mDataSetListeners.get(i).onDataSetChanged();
        }
    }

    public interface OnQueryLoadListener<T> {

        public void onLoaded(
                List<T> objects, Exception e);

        public void onLoading();
    }

    private final List<OnQueryLoadListener<T>> mQueryListeners;

    public void addOnQueryLoadListener(
            OnQueryLoadListener<T> listener) {
        if (!(mQueryListeners.contains(listener))) {
            mQueryListeners.add(listener);
        }
    }

    public void removeOnQueryLoadListener(
            OnQueryLoadListener<T> listener) {
        if (mQueryListeners.contains(listener)) {
            mQueryListeners.remove(listener);
        }
    }

    private void dispatchOnLoading() {
        for (OnQueryLoadListener<T> l : mQueryListeners) {
            l.onLoading();
        }
    }

    private void dispatchOnLoaded(List<T> objects, ParseException e) {
        for (OnQueryLoadListener<T> l : mQueryListeners) {
            l.onLoaded(objects, e);
        }
    }
}