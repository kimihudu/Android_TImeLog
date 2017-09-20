package dev.android.timelog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;


public class AddJobActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private Context context;
    private static final String TAG = "ADD_JOB";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private EditText txtAddress;
    private EditText txtTitle;
    private EditText txtDes;
    private EditText txtWage;
    private EditText txtBreak;
    private EditText txtPhone;
    private Button btnPunchIn;
    private Button btnPunchOut;
    private Button btnDate;
    private FloatingActionButton btnSave;
    private Button btnHistory;

    private DateTimeFormatter sdf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private Data data;
    private User user;
    private Job job;
    private WorkDate workDate;
    private String address;
    public static final String LONG_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String SHORT_FORMAT = "yyyy/MM/dd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        context = getApplicationContext();

//       TODO: Get data parsed from login
        Bundle exstra = getIntent().getExtras();
        String fileName = exstra.getString("user");
        final String mode = exstra.getString("mode");
        final String selectedJob = exstra.getString("data");
        final String devMode = exstra.getString("user").substring(0, 3);
        data = new Data(context, fileName);
        user = (User) data.getData(User.class);
        final DateTime currentTime = new DateTime();

//        TODO: GEO service
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

//        TODO: controls and actions


        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtDes = (EditText) findViewById(R.id.txtDes);
        txtWage = (EditText) findViewById(R.id.txtWage);
        txtBreak = (EditText) findViewById(R.id.txtBreak);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnPunchIn = (Button) findViewById(R.id.btnPunchIn);
        btnPunchOut = (Button) findViewById(R.id.btnPunchOut);
        btnSave = (FloatingActionButton) findViewById(R.id.btnSave);

        loadControlData(mode, selectedJob);

//            TODO: make call without request permission
        txtPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (txtPhone.getText() != null) {
                    String phoneNumber = txtPhone.getText().toString();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
                    return true;
                }
                return false;
            }
        });

        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setText(data.time2String(currentTime, SHORT_FORMAT));
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//            TODO: auto geolocation
        txtAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (address != null) {
                    txtAddress.setText(address);
                    return true;
                }
                return false;
            }
        });


        btnPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime timeIn = new DateTime();
                btnPunchIn.setText(data.time2String(timeIn, LONG_FORMAT));
            }
        });


        btnPunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime timeOut = new DateTime();
                btnPunchOut.setText(data.time2String(timeOut, LONG_FORMAT));
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //test data


                workDate = new WorkDate();
                if (devMode.equals("dev")) {
                    DateTime dt = new DateTime();
                    workDate.setTimeIn(data.time2String(dt, LONG_FORMAT));
                    workDate.setTimeOut(data.time2String(dt.plusHours(4), LONG_FORMAT));
                } else {
                    workDate.setTimeIn(btnPunchIn.getText().toString());
                    workDate.setTimeOut(btnPunchOut.getText().toString());
                }
                workDate.setwDate(btnDate.getText().toString());
                workDate.setBreakTime(txtBreak.getText().toString());

                job = new Job();
                job.setName(txtTitle.getText().toString());
                job.setAddress(txtAddress.getText().toString());
                job.setRate(Float.valueOf(txtWage.getText().toString()));
                job.setDes(txtDes.getText().toString());
                job.getwDates().add(workDate);
                job.setwHours(job.calwHours());
                job.setSalary(job.calSalary());
                job.setPhone(txtPhone.getText().toString());

                switch (mode) {
                    case "EDIT":
                        user.editJob(context, user.getUsrName(), selectedJob, job);
                        break;
                    case "ADD":
                        user.addJob(context, user.getUsrName(), job);
                        break;
                }

                Intent i = new Intent(AddJobActivity.this, ListJobsActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HistoryActivity.class);
                i.putExtra("user", user.getUsrName());
                i.putExtra("selectedJob", selectedJob);
                startActivity(i);
            }
        });
    }

    private void loadControlData(String mode, String selectedData) {
        if (mode.equals("EDIT")) {

            for (Job _job : user.getJobs()) {
                if (_job.getName().equals(selectedData)) {
                    txtTitle.setText(_job.getName());
                    txtAddress.setText(_job.getAddress());
                    txtWage.setText(Float.toString(_job.getRate()));
                    txtDes.setText(_job.getDes());
                    txtPhone.setText(_job.getPhone());
                    btnHistory.setEnabled(true);
                }
            }
        }
    }

    //    TODO: override sections
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

//            TODO: get address from latlng returned by google api
            address = getAddress(mLocation.getLatitude(), mLocation.getLongitude());

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");

    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        TODO: keep track location change
        address = getAddress(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //    TODO: get address from latlng google location
    private String getAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("current location", "" + strReturnedAddress.toString());
            } else {
                Log.w("current location", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("current location", "Canont get Address!");
        }
        return strAdd;
    }
}
