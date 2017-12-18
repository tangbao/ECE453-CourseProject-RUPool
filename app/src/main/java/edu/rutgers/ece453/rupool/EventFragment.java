package edu.rutgers.ece453.rupool;

import android.Manifest;
import android.accounts.AccountManager;
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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static edu.rutgers.ece453.rupool.Constant.GET_USER_SUCCESS;


public class EventFragment extends Fragment
        implements EasyPermissions.PermissionCallbacks {
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String ARGS_POOLACTIVITY = "edu.rutgers.ece453.rupool.EventFragment.ARGS.POOLACTIVITY";
    private static final String TAG = "Event Fragment";
    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
    Button joinButton;
    Button quitButton;
    boolean isJoined = false; //判断是否已经参加
    GoogleAccountCredential mCredential;
    private PoolActivity mPoolActivity;
    private TextView mTextViewDestination;
    private TextView mTextViewDate;
    private TextView mTextViewNumberOfPassenger;
    private TextView mTextViewPrice;
    private TextView mTextViewDescription;
    private TextView mTextViewStartPoint;
    private OnFragmentInteractionListener mListener;
    private FirebaseUser firebaseUser;
    private User myUser;
    private DatabaseUtils databaseUtils;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;



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

        // add by tangbao
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseUtils = new DatabaseUtils();

        databaseUtils.getUser(firebaseUser.getUid(), 1, new Interface.OnGetUserListener() {
            @Override
            public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                if (RESULT_CODE == GET_USER_SUCCESS) {
                    myUser = user;
                } else {
                    //todo 没有在数据库中找到用户的异常处理
                }
            }
        });

        if (firebaseUser != null) {

            for (String id : mPoolActivity.getMembers()) {
                if (id.equals(firebaseUser.getUid())) {
                    isJoined = true;
                    break;
                }
            }
        } else {
            //todo 当前没有用户登陆的异常处理
        }
        // add end


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

//        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
//        if (fab != null) {
//            fab.show();
//        }


        joinButton = view.findViewById(R.id.Join);
        quitButton = view.findViewById(R.id.Quit);

        // add and edit by tangbao
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPoolActivity.getStatus()) {
                    mPoolActivity.addMember(firebaseUser.getUid());
                    Log.e(TAG, mPoolActivity.getId());
                    myUser.joinActivity(mPoolActivity.getId());
                    if (mPoolActivity.getMembers().size() == mPoolActivity.getMaxMember()) {
                        mPoolActivity.setStatus(false);
                    }
                    databaseUtils.updateUser(myUser);
                    databaseUtils.updateActivity(mPoolActivity);
                    //todo 检查数据一致性 防止覆盖 etc

                    getResultsFromApi();

                    Toast.makeText(getContext(), "Join successfully.", Toast.LENGTH_LONG).show();

                    joinButton.setVisibility(View.INVISIBLE);
                    quitButton.setVisibility(View.VISIBLE);
                    databaseUtils.findAllUser(new Interface.OnFindAllUserListener() {
                        @Override
                        public void onFindAllUser(List<User> lu, int RESULT_CODE) {
                            Map<String, User> stringUserMap = new HashMap<>();
                            for (User user : lu)
                                stringUserMap.put(user.getUid(), user);
                            List<User> users = new LinkedList<>();
                            for (String s : mPoolActivity.getMembers())
                                if (stringUserMap.containsKey(s))
                                    users.add(stringUserMap.get(s));

                            mAdapter = new AdapterRecyclerViewUsers(users, new AdapterRecyclerViewUsers.OnItemClickListener() {
                                @Override
                                public void onItemClick(User user) {
                                    mListener.startProfile(user);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                } else {
                    Log.e(TAG, "Error: PoolActivity Closed.");
                    Toast.makeText(getContext(), "Error: PoolAcitivity Closed", Toast.LENGTH_LONG).show();
                }
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser.getUid().equals(mPoolActivity.getSponsorId())){
                    Toast.makeText(getContext(), "You cannot quit the activity you created.", Toast.LENGTH_LONG).show();
                }else{

                    myUser.quitActivity(mPoolActivity.getId());
                    mPoolActivity.removeMember(myUser.getUid());
                    if (!mPoolActivity.getStatus()) {
                        mPoolActivity.setStatus(true);
                    }
                    databaseUtils.updateUser(myUser);
                    databaseUtils.updateActivity(mPoolActivity);

                    Toast.makeText(getContext(), "Quit successfully.", Toast.LENGTH_LONG).show();

                    joinButton.setVisibility(View.VISIBLE);
                    quitButton.setVisibility(View.INVISIBLE);
                    databaseUtils.findAllUser(new Interface.OnFindAllUserListener() {
                        @Override
                        public void onFindAllUser(List<User> lu, int RESULT_CODE) {
                            Map<String, User> stringUserMap = new HashMap<>();
                            for (User user : lu)
                                stringUserMap.put(user.getUid(), user);
                            List<User> users = new LinkedList<>();
                            for (String s : mPoolActivity.getMembers())
                                if (stringUserMap.containsKey(s))
                                    users.add(stringUserMap.get(s));

                            mAdapter = new AdapterRecyclerViewUsers(users, new AdapterRecyclerViewUsers.OnItemClickListener() {
                                @Override
                                public void onItemClick(User user) {
                                    mListener.startProfile(user);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });

        view.findViewById(R.id.contact)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseUtils databaseUtils = new DatabaseUtils();
                        databaseUtils.getUser(mPoolActivity.getSponsorId(), 123, new Interface.OnGetUserListener() {
                            @Override
                            public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                                if (RESULT_CODE == Constant.GET_USER_SUCCESS) {

                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:"));
                                    // TODO
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
                                    if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                                        startActivity(intent);
                                }
                            }
                        });


                    }
                });

        // end by tangbao

        // initial textview,
        // edited by tb

        mTextViewDestination = view.findViewById(R.id.event_dest);
        mTextViewDate = view.findViewById(R.id.event_date);
        mTextViewNumberOfPassenger = view.findViewById(R.id.event_num);
        mTextViewPrice = view.findViewById(R.id.event_price);
        mTextViewStartPoint = view.findViewById(R.id.event_startPoint);
        mTextViewDescription = view.findViewById(R.id.event_description);


        Log.d("getDest",mPoolActivity.getDestiName());
        mTextViewDestination.setText(mPoolActivity.getDestiName());
        mTextViewNumberOfPassenger.setText(String.valueOf(mPoolActivity.getMembers().size()));
        mTextViewDate.setText(mPoolActivity.getDate());
        mTextViewPrice.setText(String.valueOf(mPoolActivity.getMoneyPerPerson()));
        mTextViewDescription.setText(mPoolActivity.getDescription());
        mTextViewStartPoint.setText(mPoolActivity.getStartPoint());

        if (!isJoined) {
            joinButton.setVisibility(View.VISIBLE);
            quitButton.setVisibility(View.INVISIBLE);
        } else {
            joinButton.setVisibility(View.INVISIBLE);
            quitButton.setVisibility(View.VISIBLE);
        }

        mRecyclerView = view.findViewById(R.id.RecyclerView_EventFragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        databaseUtils.findAllUser(new Interface.OnFindAllUserListener() {
            @Override
            public void onFindAllUser(List<User> lu, int RESULT_CODE) {
                Map<String, User> stringUserMap = new HashMap<>();
                for (User user : lu)
                    stringUserMap.put(user.getUid(), user);
                List<User> users = new LinkedList<>();
                for (String s : mPoolActivity.getMembers())
                    if (stringUserMap.containsKey(s))
                        users.add(stringUserMap.get(s));

                mAdapter = new AdapterRecyclerViewUsers(users, new AdapterRecyclerViewUsers.OnItemClickListener() {
                    @Override
                    public void onItemClick(User user) {
                        mListener.startProfile(user);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            }
        });


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


    //============below is google calender by tangbao=================

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
                getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void startProfile(User user);
    }

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
            Toast.makeText(getActivity(), "onPreExecute",Toast.LENGTH_LONG).show();
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
