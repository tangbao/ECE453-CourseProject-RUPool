package edu.rutgers.ece453.rupool;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class NewEventFragment extends Fragment {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private EditText dest;
    private EditText pricePerUser;
    private EditText userNum;
    private Button button;
    private  EditText edittext;
    private Place place;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private Spinner spinner;
    private EditText description;
    private String startLocation;


    private OnFragmentInteractionListener mListener;


    public NewEventFragment() {
        // Required empty public constructor
    }



    public static NewEventFragment newInstance(String param1, String param2) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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


        dest=view.findViewById(R.id.new_dest);
        pricePerUser=view.findViewById(R.id.new_price);
        userNum=view.findViewById(R.id.new_num);
        button=view.findViewById(R.id.OK);
        spinner=view.findViewById(R.id.spinner);
        description=view.findViewById(R.id.new_description);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.startPoint, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        startLocation="Busch Student Center";
                        break;
                    case 1:
                        startLocation="Liv Student Center";
                        break;
                    case 2:
                        startLocation="Cook Student Center";
                        break;
                    case 3:
                        startLocation="Douglas Student Center";
                        break;
                }
                Log.d("startLocation",startLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO:Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO:Handle the error.
                }

            }
        });


        myCalendar= Calendar.getInstance();

        edittext= (EditText) view.findViewById(R.id.new_date);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            public void updateLabel() {
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edittext.setText(sdf.format(myCalendar.getTime()));
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
                String inputDescription=description.getText().toString();

                if(inputDate.matches("")||inputDest.matches("")
                        ||inputDest.matches("")||inputNum.matches("")
                        ||inputPrice.matches("") ||inputDescription.matches("")){
                    Toast.makeText(getActivity(),"You have to input all the information",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //TODO 创建poolactivity并加入数据库中，获取到的place 变量名为place ，直接加进去就行
                    //add by tb
                    PoolActivity pa = new PoolActivity("name", FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            inputDate,"Description", "startPoint", Integer.parseInt(inputNum), Double.parseDouble(inputPrice));
                    pa.setPlace(place);
                    DatabaseUtils du = new DatabaseUtils();
                    du.addActivity(pa);
                    //end by tb
                    onBackPressed();
                }
            }
        });


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(getActivity(), data);
                dest.setText(place.getName());
                Log.i("AUTO", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);

                // TODO:Handle the error.
                Log.i("AUTO", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
