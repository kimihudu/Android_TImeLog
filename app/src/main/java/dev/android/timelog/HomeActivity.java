package dev.android.timelog;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private Data data;
    private User user;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this.getApplicationContext();

        initView();

        Button btnViewJobs = (Button) findViewById(R.id.btnViewJobs);
        btnViewJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ListJobsActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });

        Button btnViewSummary = (Button) findViewById(R.id.btnViewSummary);
        btnViewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SummaryActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });

        FloatingActionButton btnAddJob = (FloatingActionButton) findViewById(R.id.btnAddJob);
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AddJobActivity.class);
                i.putExtra("user", user.getUsrName());
                i.putExtra("mode", "ADD");
                startActivity(i);
            }
        });

        Button btnSetting = (Button)findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SettingsActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });
    }

    private void initView() {

//      TODO: Get data parsed from login
        Bundle exstra = getIntent().getExtras();
        data = new Data(context, exstra.getString("user"));
        user = (User) data.getData(User.class);

        TextView txvUser = (TextView)findViewById(R.id.txvUserName);
        txvUser.setText(user.getUsrName());
        TextView txvCurrentDate = (TextView) findViewById(R.id.txvCurrentDate);
        DateTime currentDate = new DateTime();
        txvCurrentDate.setText(data.time2String(currentDate,"HH:mm yyyy-MM-dd"));

    }
}
