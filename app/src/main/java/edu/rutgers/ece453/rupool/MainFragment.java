package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.lang.reflect.Array;
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

        FloatingActionButton fab=((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.show();
        }

        listView=(ListView) view.findViewById(R.id.myList);
        ArrayList<PoolActivity> poolActivities=new ArrayList<>();

        poolActivities.add(new PoolActivity("Test","1",4,"Dec","Liv",5.0));
        poolActivities.add(new PoolActivity("Test","1",4,"Dec","Liv",5.0));
        poolActivities.add(new PoolActivity("Test","1",4,"Dec","Liv",5.0));
        poolActivities.add(new PoolActivity("Test","1",4,"Dec","Liv",5.0));

        myBaseAdapter=new MyBaseAdapter(getActivity(),R.layout.item_layout,poolActivities);
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


    public class MyBaseAdapter extends ArrayAdapter<PoolActivity> {
        private ArrayList<PoolActivity> task;
        //private Context mContext;
        private LayoutInflater inflater;
        private int mResourceId;

//        public MyBaseAdapter(Context context,ArrayList<PoolActivity> _task) {
//            super(context,R.layout.item_layout,_task);
//            this.mContext = context;
//            //this.textViewResourceId=textViewResourceId;
//            this.task=_task;
//
//        }

        public MyBaseAdapter(Context context, int resource, ArrayList<PoolActivity> _task)
        {
            super(context,resource,_task);
           // this.mContext = context;
            this.mResourceId=resource;
            this.task=_task;
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PoolActivity poolActivity= task.get(position);

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater=LayoutInflater.from(getActivity().getApplicationContext());
                convertView = layoutInflater.inflate(mResourceId,null);

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
            holder.dateDay.setText("16");
            holder.dateMonth.setText("Dec");
            holder.description.setText("老王开车去东北");
            holder.startPoint.setText("Piscataway");
            holder.destination.setText("Liv");


            return convertView;
        }

        /**
         * ViewHolder类用以储存item中控件的引用
         */

    }
    static class ViewHolder {
        TextView dateMonth;
        TextView dateDay;
        TextView startPoint;
        TextView destination;
        TextView description;
    }
}
