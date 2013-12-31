package com.dublet.celicadb2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A list fragment representing a list of Cars. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link CarDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class CarListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private ArrayList<Car> _cars = null;
    public static final int SORT_AGE = 1, SORT_NAME = 2, SORT_CODE = 3, SORT_TOP_SPEED = 4, SORT_ACCELERATION = 5, SORT_POWER_TO_WEIGHT = 6;
    public static final int FILTER_NONE = 1, FILTER_GENERATION = 2;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CarListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (_cars == null)
        {
            _cars = CarFactory.getInstance(getActivity()).getCarList();
        }

        setListAdapter(new ArrayAdapter<Car>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, _cars));
        int sorting = SORT_AGE;
        try {
            SharedPreferences settings = getActivity().getSharedPreferences(Preferences.PREFS_NAME, 0);
            sorting = Integer.parseInt(settings.getString("sorting", "1"));
        }
        catch (ClassCastException e) { /* Do nothing */ }
        catch (Exception e) { Log.e("NPE", e.getMessage()); }
        sortList(sorting);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

        mCallbacks.onItemSelected(((Car)getListAdapter().getItem(position)).code);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @SuppressWarnings("unchecked")
    public void sortList(int sortType) {
        ArrayAdapter<Car> arrayAdapter;
        try {
            arrayAdapter = (ArrayAdapter<Car>)getListAdapter();
        } catch (ClassCastException e) {
            return;
        }
        switch (sortType) {
            case SORT_AGE:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        if (c1.generation < c2.generation)
                            return -1;
                        if (c1.generation > c2.generation)
                            return 1;
                        if (c1.releaseYear < c2.releaseYear)
                            return -1;
                        if (c1.releaseYear > c2.releaseYear)
                            return 1;
                        if (c1.deceaseYear < c2.deceaseYear)
                            return -1;
                        if (c1.deceaseYear > c2.deceaseYear)
                            return 1;
                        return 0;
                    }
                });
                break;
            case SORT_NAME:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        return c1.name.compareTo(c2.name);
                    }
                });
                break;
            case SORT_CODE:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        return c1.code.compareTo(c2.code);
                    }
                });
                break;
            case SORT_TOP_SPEED:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        Float t1 = c1.getTopSpeed(), t2 = c2.getTopSpeed();
                        if (t1.isNaN() && t2.isNaN())
                            return 0;
                        /* Consider NaN top speed to be the smallest */
                        if (t1.isNaN() && !t2.isNaN())
                            return -1;
                        if (!t1.isNaN() && t2.isNaN())
                            return 1;
                        if (t1 < t2)
                            return -1;
                        if (t1 > t2)
                            return 1;

                        return 0;
                    }
                });
                break;
            case SORT_ACCELERATION:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        Float t1 = c1.getAcceleration(), t2 = c2.getAcceleration();
                        if (t1.isNaN() && t2.isNaN())
                            return 0;
                        /* Consider NaN acceleration to be the highest */
                        if (t1.isNaN() && !t2.isNaN())
                            return 1;
                        if (!t1.isNaN() && t2.isNaN())
                            return -1;
                        if (t1 < t2)
                            return -1;
                        if (t1 > t2)
                            return 1;
                        return 0;
                    }
                });
                break;
            case SORT_POWER_TO_WEIGHT:
                arrayAdapter.sort(new Comparator<Car>() {
                    public int compare(Car c1, Car c2) {
                        Float t1 = c1.getPowerPerWeight(), t2 = c2.getPowerPerWeight();
                        if (t1.isNaN() && t2.isNaN())
                            return 0;
                        /* Consider NaN p/w to be the smallest */
                        if (t1.isNaN() && !t2.isNaN())
                            return -1;
                        if (!t1.isNaN() && t2.isNaN())
                            return 1;
                        if (t1 < t2)
                            return -1;
                        if (t1 > t2)
                            return 1;
                        return 0;
                    }
                });
                break;

        }
        arrayAdapter.notifyDataSetChanged();
    }
    public void filterList(int filterType) {
        switch (filterType) {
            case FILTER_NONE:
                ((ArrayAdapter)getListAdapter()).getFilter();
                break;
            case FILTER_GENERATION:
               /* ((ArrayAdapter)getListAdapter()).sort(new Comparator() {
                    public int compare(Object o, Object o2) {
                        Car c1 = (Car)o, c2 = (Car)o2;
                        return c1.name.compareTo(c2.name);
                    }
                });*/
                break;
        }
        ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
