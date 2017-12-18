package edu.rutgers.ece453.rupool;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static edu.rutgers.ece453.rupool.Constant.GET_ALL_ACTIVITY_SUCCESS;


public class MainFragment extends Fragment {


    //by tb
    private final static String TAG = "Main Fragment";
    List<PoolActivity> poolActivities;
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
    private String timeFromWhen;
    private String timeToWhen;
    private OnFragmentInteractionListener mListener;

    //    private OnFragmentInteractionListener mListener;
    private List<PoolActivity> allActivityList;
    private AdapterRecyclerViewMainFragment.OnItemClickListener mOnItemClickListener =
            new AdapterRecyclerViewMainFragment.OnItemClickListener() {
                @Override
                public void onItemClick(PoolActivity poolActivity) {
                    Log.d(TAG, "onItemClick: Start event activity.");
                    mListener.startEventActivity(poolActivity);
                }
            };
    //end by tb

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
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

//        if(((MainActivity) getActivity()).getAllActivityList()!=null) {
//            allActivityList = ((MainActivity) getActivity()).getAllActivityList();
//            Toast.makeText(getActivity().getApplicationContext(),"Fragment fOUNT",Toast.LENGTH_SHORT).show();
//        }

        fromWhen = view.findViewById(R.id.time_filter1);
        toWhen = view.findViewById(R.id.time_filter2);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.show();
        }

        // set the time filter

        myCalendarFrom = Calendar.getInstance();

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

        myCalendarTo = Calendar.getInstance();

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


        //TEST CODE by tangbao
        poolActivities = new ArrayList<PoolActivity>();
        mRecyclerView = view.findViewById(R.id.RecyclerView_MainFragment);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DatabaseUtils du = new DatabaseUtils();
        du.findAllActivity(new Interface.OnFindAllActivityListener() {
            @Override
            public void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE) {
                for (int i = 0; i < lpa.size(); i++) {
                    Log.e(TAG, lpa.get(i).getId());
                }
                if (RESULT_CODE == GET_ALL_ACTIVITY_SUCCESS) {

                    // 数据库不为空，读到了数据
                    poolActivities = lpa;

                    // 排序
                    Collections.sort(poolActivities, new Comparator<PoolActivity>() {
                        @Override
                        public int compare(PoolActivity poolActivity, PoolActivity t1) {
                            String dateString1 = poolActivity.getDate();
                            String dateString2 = t1.getDate();

                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                            Date date1 = null;
                            Date date2 = null;

                            try {
                                date1 = format.parse(dateString1);
                                date2 = format.parse(dateString2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return (date1.compareTo(date2));
                        }
                    });


                    mAdapter = new AdapterRecyclerViewMainFragment(poolActivities, mOnItemClickListener);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    //列表为空
                    // set up RecyclerView

                    mAdapter = new AdapterRecyclerViewMainFragment(poolActivities, mOnItemClickListener);
                    mRecyclerView.setAdapter(mAdapter);
                }

            }
        });
        //TEST CODE end


        // 时间过滤

        // image button来确定
        imgBtn = view.findViewById(R.id.imgBtn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 确定设置time filter

                if (fromWhen.getText() != null && toWhen.getText() != null) {
                    timeFromWhen = fromWhen.getText().toString();
                    timeToWhen = toWhen.getText().toString();


                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    Date dateFromWhen = null;
                    Date dateToWhen = null;

                    try {
                        dateFromWhen = format.parse(timeFromWhen);
                        dateToWhen = format.parse(timeToWhen);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //得到了前后两个date

                    ArrayList<PoolActivity> tempActivityList = new ArrayList<PoolActivity>();
                    //遍历时间
                    for (int i = 0; i < poolActivities.size(); i++) {
                        PoolActivity tempActivity = poolActivities.get(i);

                        // 转变date string的格式
                        String tempDate = tempActivity.getDate().toString();
                        Date currentDate = null;

                        try {
                            currentDate = format.parse(tempDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        if (currentDate.before(dateToWhen) && currentDate.after(dateFromWhen)) {
                            tempActivityList.add(tempActivity);
                        }


                    }

                    mAdapter = new AdapterRecyclerViewMainFragment(tempActivityList, mOnItemClickListener);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
        });


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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    void updateRecyclerView(List<PoolActivity> poolActivities) {
        mAdapter = new AdapterRecyclerViewMainFragment(poolActivities, mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void startEventActivity(PoolActivity poolActivity);
    }

}

