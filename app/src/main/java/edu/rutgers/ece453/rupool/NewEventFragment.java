package edu.rutgers.ece453.rupool;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ErrorDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NewEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewEventFragment() {
        // Required empty public constructor
    }



    public static NewEventFragment newInstance(String param1, String param2) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_new_event, container, false);


        FloatingActionButton fab=((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.hide();
        }



        final EditText dest=view.findViewById(R.id.new_dest);
        final EditText pricePerUser=view.findViewById(R.id.new_price);
        final EditText userNum=view.findViewById(R.id.new_num);
        Button button=view.findViewById(R.id.OK);




        final Calendar myCalendar = Calendar.getInstance();

        final EditText edittext= (EditText) view.findViewById(R.id.new_date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            private void updateLabel() {
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edittext.setText(sdf.format(myCalendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }


        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputDate=edittext.getText().toString();
                String inputDest=dest.getText().toString();
                String inputPrice=pricePerUser.getText().toString();
                String inputNum=userNum.getText().toString();
                if(inputDate.matches("")||inputDest.matches("")
                        ||inputDest.matches("")||inputNum.matches("")){
                    Toast.makeText(getActivity(),"You have to input all the information",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //TODO 创建poolactivity并加入数据库中
                    onBackPressed();
                }
            }
        });





        return view;
    }

    public void onBackPressed(){
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {

            //( "stackzeo");
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
            removeCurrentFragment();
            //("stacknotzeo");
        }
    }

    public void removeCurrentFragment() {

        NewEventFragment currentFrag = (NewEventFragment) getFragmentManager()
                .findFragmentById(R.id.new_event);

        if (currentFrag != null)
            getFragmentManager().beginTransaction().remove(currentFrag);

        getFragmentManager().beginTransaction().commit();

    }




//    public void onButtonPressed() {
////        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
////        }
//        Toast.makeText(getActivity(),"Back",Toast.LENGTH_SHORT).show()  ;
//        //super.o
//    }

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
