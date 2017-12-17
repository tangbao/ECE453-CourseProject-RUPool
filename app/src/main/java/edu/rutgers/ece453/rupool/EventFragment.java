package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static edu.rutgers.ece453.rupool.Constant.GET_USER_SUCCESS;


public class EventFragment extends Fragment {
    private static final String ARGS_POOLACTIVITY = "edu.rutgers.ece453.rupool.EventFragment.ARGS.POOLACTIVITY";
    private static final String TAG = "Event Fragment";
    Button joinButton;
    Button quitButton;
    boolean isJoined = false; //判断是否已经参加
    private PoolActivity mPoolActivity;
    private TextView mTextViewDestination;
    private TextView mTextViewDate;
    private TextView mTextViewNumberOfPassenger;
    private TextView mTextViewPrice;
    private OnFragmentInteractionListener mListener;
    private FirebaseUser firebaseUser;
    private User myUser;
    private DatabaseUtils databaseUtils;

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

        // add by tangbao
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseUtils = new DatabaseUtils();
        User a = new User(firebaseUser.getUid(), "F");
        databaseUtils.addUser(a);

        databaseUtils.getUser(firebaseUser.getUid(), 1, new Interface.OnGetUserListener() {
            @Override
            public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                if(RESULT_CODE == GET_USER_SUCCESS){
                    myUser = user;
                }else{
                    //todo 没有在数据库中找到用户的异常处理
                }
            }
        });

        if(firebaseUser!=null){
            if (getArguments() != null) {
                mPoolActivity = (PoolActivity) getArguments().getSerializable(ARGS_POOLACTIVITY);
                for(String id : mPoolActivity.getMembers()){
                    if(id.equals(firebaseUser.getUid())){
                        isJoined = true;
                        break;
                    }
                }
            }else{
                //todo 没有获得Activity的异常处理
            }
        }else{
            //todo 当前没有用户登陆的异常处理
        }
        // add end

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

        // add and edit by tangbao
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPoolActivity.getStatus()){
                    mPoolActivity.addMember(firebaseUser.getUid());
                    Log.e(TAG, mPoolActivity.getId());
                    myUser.joinActivity(mPoolActivity.getId());
                    if(mPoolActivity.getMembers().size() == mPoolActivity.getMaxMember()){
                        mPoolActivity.setStatus(false);
                    }
                    databaseUtils.updateUser(myUser);
                    databaseUtils.updateActivity(mPoolActivity);
                    //todo 检查数据一致性 防止覆盖 etc

                    Toast.makeText(getContext(), "Join successfully.", Toast.LENGTH_LONG).show();

                    joinButton.setVisibility(View.INVISIBLE);
                    quitButton.setVisibility(View.VISIBLE);
                }else{
                    Log.e(TAG, "Error: PoolActivity Closed.");
                    Toast.makeText(getContext(), "Error: PoolAcitivity Closed", Toast.LENGTH_LONG).show();
                }
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUser.quitActivity(mPoolActivity.getId());
                mPoolActivity.removeMember(myUser.getUid());
                if(!mPoolActivity.getStatus()){
                    mPoolActivity.setStatus(true);
                }
                databaseUtils.updateUser(myUser);
                databaseUtils.updateActivity(mPoolActivity);

                Toast.makeText(getContext(), "Quit successfully.", Toast.LENGTH_LONG).show();

                joinButton.setVisibility(View.VISIBLE);
                quitButton.setVisibility(View.INVISIBLE);
            }
        });

        // end by tangbao

        // initial textview

        mTextViewDestination = view.findViewById(R.id.destination);
        mTextViewDate = view.findViewById(R.id.date);
        mTextViewNumberOfPassenger = view.findViewById(R.id.numberOfPassenger);
        mTextViewPrice = view.findViewById(R.id.price);

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
