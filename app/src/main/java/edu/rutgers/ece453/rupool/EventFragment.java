package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EventFragment extends Fragment {
    private static final String ARGS_POOLACTIVITY = "edu.rutgers.ece453.rupool.EventFragment.ARGS.POOLACTIVITY";
    Button joinButton;
    Button quitButton;
    boolean isJoined = false;
    private PoolActivity mPoolActivity;
    private TextView mTextViewDestination;
    private TextView mTextViewDate;
    private TextView mTextViewNumberOfPassenger;
    private TextView mTextViewPrice;
    private OnFragmentInteractionListener mListener;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(PoolActivity poolActivity) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_POOLACTIVITY, poolActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPoolActivity = (PoolActivity) getArguments().getSerializable(ARGS_POOLACTIVITY);
        }
        // TODO: 如果为空，先随便new了个activity，防止报错
        else
        {
            mPoolActivity=new PoolActivity("sun","1","11/1/2017","kaiche qu dong bei","Bush Student Center",4,3.0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.show();
        }


        joinButton = view.findViewById(R.id.Join);
        quitButton = view.findViewById(R.id.Quit);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        // initial textview

        mTextViewDestination = view.findViewById(R.id.event_dest);
        mTextViewDate = view.findViewById(R.id.event_date);
        mTextViewNumberOfPassenger = view.findViewById(R.id.event_num);
        mTextViewPrice = view.findViewById(R.id.event_price);

        // TODO 显示信息
        mTextViewDestination.setText(mPoolActivity.getDestiName());
        mTextViewNumberOfPassenger.setText(String.valueOf(mPoolActivity.getMembers().size()));
        mTextViewDate.setText(mPoolActivity.getDate());
        // TODO 此处存疑
        mTextViewPrice.setText(String.valueOf(mPoolActivity.getMoneyPerPerson()));

        if (!isJoined) {
            joinButton.setVisibility(View.VISIBLE);
            quitButton.setVisibility(View.INVISIBLE);
        } else {
            joinButton.setVisibility(View.INVISIBLE);
            quitButton.setVisibility(View.VISIBLE);
        }
        return view;
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
}
