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

        // initial textview

        mTextViewDestination = view.findViewById(R.id.destination);
        mTextViewDate = view.findViewById(R.id.date);
        mTextViewNumberOfPassenger = view.findViewById(R.id.numberOfPassenger);
        mTextViewPrice = view.findViewById(R.id.price);

        // TODO 显示信息


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
