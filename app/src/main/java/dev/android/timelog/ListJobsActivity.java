package dev.android.timelog;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class ListJobsActivity extends AppCompatActivity {

    ListView lvJobs;
    Context context;
    Data data;
    User user;
    private Button edit;
    private Button delete;
    ListJobAdapter adapter;
    HashMap<String,String> selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs);
        context = this.getApplicationContext();

//               TODO: Get data parsed from login
        Bundle exstra = getIntent().getExtras();

        data = new Data(context, exstra.getString("user"));
        user = (User) data.getData(User.class);

        lvJobs = (ListView) findViewById(R.id.lvJobs);
        showJobs(user);
//        lvJobs.setItemsCanFocus(false);
        lvJobs.setOnItemLongClickListener(new OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                int pos = position + 1;
//                Toast.makeText(context, data.get("JOB_NAME") + " Clicked", Toast.LENGTH_SHORT).show();
                selectedData =  (HashMap<String,String>)adapter.getItem(position);
                Intent i = new Intent(ListJobsActivity.this, AddJobActivity.class);
                i.putExtra("user", user.getUsrName());
                i.putExtra("mode","EDIT");
                i.putExtra("data",selectedData.get("JOB_NAME"));
                startActivity(i);
                return true;
            }
        });

        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListJobsActivity.this, HomeActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });

        FloatingActionButton btnAddJob = (FloatingActionButton) findViewById(R.id.btnAddJob);
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddJobActivity.class);
                i.putExtra("user", user.getUsrName());
                i.putExtra("mode", "ADD");
                startActivity(i);
            }
        });

    }

    //      TODO: bind data for listview
    public void showJobs(User user) {

        ArrayList list = new ArrayList<HashMap<String, String>>();
        for (Job job : user.getJobs()) {
            HashMap<String, String> infoJob = new HashMap<String, String>();
            infoJob.put("JOB_NAME", job.getName());
            infoJob.put("JOB_RATE", Float.toString(job.getRate()));
            infoJob.put("JOB_LOC", job.getAddress());
            infoJob.put("JOB_DES", job.getDes());
            infoJob.put("JOB_PHONE", job.getPhone());
            infoJob.put("JOB_OWNER", user.getUsrName());

            list.add(infoJob);
        }

        adapter = new ListJobAdapter(this, list);
//        adapter.list.size();
        lvJobs.setAdapter(adapter);
    }
}
