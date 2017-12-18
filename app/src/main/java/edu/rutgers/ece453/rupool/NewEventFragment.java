package edu.rutgers.ece453.rupool;

import android.*;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class NewEventFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String TAG = "New Event Fragment";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
    GoogleAccountCredential mCredential;

    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
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

    private PoolActivity mPoolActivity;


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


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_new_event, container, false);



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

        edittext = view.findViewById(R.id.new_date);
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
                    //add by tb
                    mPoolActivity = new PoolActivity("Carpool to "+place.getName().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            inputDate,inputDescription, startLocation, Integer.parseInt(inputNum), Double.parseDouble(inputPrice));
                    mPoolActivity.setPlace(place);
                    final DatabaseUtils du = new DatabaseUtils();
                    du.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), 1,
                            new Interface.OnGetUserListener() {
                                @Override
                                public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                                    if(RESULT_CODE==1){
                                        user.joinActivity(mPoolActivity.getId());
                                        du.updateUser(user);
                                    }else{
                                        //todo 没找到用户的异常处理
                                    }
                                }
                            });
                    du.addActivity(mPoolActivity);

                    //todo 添加到谷歌日历
                    Toast.makeText(getContext(), "Start to add the event to your Google Calender", Toast.LENGTH_LONG).show();
                    getResultsFromApi();

                    //end by tb
//                    onBackPressed();
                    mListener.updateMainFragmentList();
                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        if (fab != null) {
            fab.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"start on activity result "+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");

                    Toast.makeText(getActivity(), "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;

            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
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
                break;
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
        void updateMainFragmentList();

    }

    //============below is google calender by tangbao=================


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            //mOutputText.setText("No network connection available.");
            Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            Log.e("TAG","start to enter make request task");
            new MakeRequestTask(mCredential).execute();

        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getActivity(), android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                Log.e(TAG,"Trying to start activity for result");
                try{
                    startActivityForResult(
                            mCredential.newChooseAccountIntent(),
                            REQUEST_ACCOUNT_PICKER);
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void startProfile(User user);
//    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("RUPool")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG,"enter do in bg");
            try {
                Event event = new Event()
                        .setSummary(mPoolActivity.getName())
                        .setLocation(mPoolActivity.getDestiAddress())
                        .setDescription(mPoolActivity.getDescription());

                //DateTime dateTime = new DateTime("2017-12-27T10:00:00-07:00");
                DateTime dateTime = new DateTime(DataTimeUtil.dateForDateTime(mPoolActivity.getDate()));
                EventDateTime time = new EventDateTime()
                        .setDateTime(dateTime);
                event.setStart(time).setEnd(time);

                String ID = "primary";

                mService.events().insert(ID, event).execute();
                Log.e(TAG, "inserting event");
                //Toast.makeText(getContext(), "Add event to your google calender successfully", Toast.LENGTH_LONG).show();
                return null;
            } catch (Exception e) {
                mLastError = e;
                Log.e(TAG, e.toString());
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
//
//            Event event = new Event()
//                    .setSummary(mPoolActivity.getName())
//                    .setLocation(mPoolActivity.getDestiAddress())
//                    .setDescription(mPoolActivity.getDescription());
//
//            DateTime dateTime = new DateTime(mPoolActivity.getDate());
//            EventDateTime time = new EventDateTime()
//                    .setDate(dateTime);
//            event.setStart(time).setEnd(time);
//
//            String ID = "primary";
//            mService.events().insert(ID, event).execute();
//            Log.e(TAG, "inserting event");


//            DateTime now = new DateTime(System.currentTimeMillis());
//            List<String> eventStrings = new ArrayList<String>();
//            Events events = mService.events().list("primary")
//                    .setMaxResults(10)
//                    .setTimeMin(now)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .execute();
//            List<Event> items = events.getItems();
//
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    // All-day events don't have start times, so just use
//                    // the start date.
//                    start = event.getStart().getDate();
//                }
//                eventStrings.add(
//                        String.format("%s (%s)", event.getSummary(), start));
//            }
//            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            //mOutputText.setText("");
            //mProgress.show();
            //Toast.makeText(getActivity(), "onPreExecute",Toast.LENGTH_LONG).show();
        }

//        @Override
//        protected void onPostExecute(List<String> output) {
//            //mProgress.hide();
//            if (output == null || output.size() == 0) {
//                //mOutputText.setText("No results returned.");
//                Toast.makeText(getActivity(), "No results returned.",Toast.LENGTH_LONG).show();
//            } else {
//                output.add(0, "Data retrieved using the Google Calendar API:");
//                //mOutputText.setText(TextUtils.join("\n", output));
//                Toast.makeText(getActivity(), "\n"+output,Toast.LENGTH_LONG).show();
//            }
//        }

        @Override
        protected void onCancelled() {
            //mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());

                    Toast.makeText(getActivity(), "The following error occurred:\n"+
                            mLastError.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                //mOutputText.setText("Request cancelled.");
                Toast.makeText(getActivity(), "Request cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
