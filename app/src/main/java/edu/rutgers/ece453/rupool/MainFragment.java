package edu.rutgers.ece453.rupool;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainFragment extends Fragment {


    private ArrayAdapter<String> searchResult;
    private boolean isJoined = false;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText fromWhen;
    private EditText toWhen;
    private Calendar myCalendarFrom;
    private DatePickerDialog.OnDateSetListener dateFrom;
    private Calendar myCalendarTo;
    private DatePickerDialog.OnDateSetListener dateTo;
    private ImageButton imgBtn;
    private List<PoolActivity> allActivityList;

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

        if(((MainActivity) getActivity()).getAllActivityList()!=null) {
            allActivityList = ((MainActivity) getActivity()).getAllActivityList();
            Toast.makeText(getActivity().getApplicationContext(),"Fragment fOUNT",Toast.LENGTH_SHORT).show();
        }


        fromWhen=(EditText) view.findViewById(R.id.time_filter1);
        toWhen=(EditText) view.findViewById(R.id.time_filter2);

        FloatingActionButton fab=((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.show();
        }

        ArrayList<PoolActivity> poolActivities=new ArrayList<>();

        poolActivities.add(new PoolActivity("Test1", "1", "now", "test description", "BSC", 5, 5.0));
        poolActivities.add(new PoolActivity("Test2", "2", "now", "test description", "BSC", 5, 5.0));
        poolActivities.add(new PoolActivity("Test3", "3", "now", "test description", "BSC", 5, 5.0));
        poolActivities.add(new PoolActivity("Test4", "4", "now", "test description", "BSC", 5, 5.0));

        // set the time filter

        myCalendarFrom= Calendar.getInstance();

        dateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarFrom.set(Calendar.YEAR, year);
                myCalendarFrom.set(Calendar.MONTH, monthOfYear);
                myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            public void updateLabel() {
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                fromWhen.setText(sdf.format(myCalendarFrom.getTime()));
            }


        };

        fromWhen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateFrom, myCalendarFrom
                        .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                        myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }


        });

        myCalendarTo= Calendar.getInstance();

        dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, monthOfYear);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            public void updateLabel() {
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                toWhen.setText(sdf.format(myCalendarTo.getTime()));
            }


        };

        toWhen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateTo, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }


        });

        // image button来确定
        imgBtn=(ImageButton) view.findViewById(R.id.imgBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 确定设置time filter

            }
        });




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

