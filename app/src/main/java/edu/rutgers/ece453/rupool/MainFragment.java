package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ListView listView;
    ArrayAdapter<String> searchResult;
    MyBaseAdapter myBaseAdapter;
    boolean isJoined=false;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        listView=(ListView) view.findViewById(R.id.myList);


//
//        //PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//        //        getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
//                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO:Get info about the selected place.
//                Log.i("AUTOCOMPLETE", "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO:Handle the error.
//                Log.i("AUTOCOMPLETE", "An error occurred: " + status);
//            }
//        });


        ArrayList<String> searchContent=new ArrayList<>();
        searchContent.addAll(Arrays.asList(getResources().getStringArray(R.array.eventList)));

        //searchResult=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,searchContent);
        myBaseAdapter=new MyBaseAdapter(getActivity());
        listView.setAdapter(myBaseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventFragment eventFragment=new EventFragment();
                getActivity().getSupportFragmentManager().popBackStack();
                android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,eventFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    public class MyBaseAdapter extends BaseAdapter {
        private int[] colors = new int[] { 0xff3cb371, 0xffa0a0a0 };
        private Context mContext;

        public MyBaseAdapter(Context context) {
            this.mContext = context;
        }


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_layout, null);

                holder.dateMonth=(TextView) convertView.findViewById(R.id.Date_Month);
                holder.dateDay=(TextView) convertView.findViewById(R.id.Date_Day);
                holder.startPoint=(TextView) convertView.findViewById(R.id.start_point);
                holder.destination=(TextView) convertView.findViewById(R.id.destination);
                holder.description=(TextView) convertView.findViewById(R.id.description);

                // 将holder绑定到convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 向ViewHolder中填入的数据

//            holder.dateMonth.setText();

            return convertView;
        }

        /**
         * ViewHolder类用以储存item中控件的引用
         */
        final class ViewHolder {
            TextView dateMonth;
            TextView dateDay;
            TextView startPoint;
            TextView destination;
            TextView description;
        }
    }
}
