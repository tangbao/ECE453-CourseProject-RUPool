package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class MainFragment extends Fragment {


    ArrayAdapter<String> searchResult;
    boolean isJoined = false;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
//    private OnFragmentInteractionListener mListener;


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

        ArrayList<PoolActivity> poolActivities=new ArrayList<>();

        poolActivities.add(new PoolActivity("Test", "1", 4, "Dec", "Liv", 5.0));
        poolActivities.add(new PoolActivity("Test", "1", 4, "Dec", "Liv", 5.0));
        poolActivities.add(new PoolActivity("Test", "1", 4, "Dec", "Liv", 5.0));
        poolActivities.add(new PoolActivity("Test", "1", 4, "Dec", "Liv", 5.0));



        // set up RecyclerView
        mRecyclerView = view.findViewById(R.id.RecyclerView_MainFragment);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterRecyclerViewMainFragment(poolActivities, new AdapterRecyclerViewMainFragment.OnItemClickListener() {
            @Override
            public void onItemClick(PoolActivity poolActivity) {
                EventFragment eventFragment = EventFragment.newInstance(poolActivity);
                getActivity().getSupportFragmentManager().popBackStack();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, eventFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }


    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }


}

