package dev.android.timelog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    Data data;
    User user;
    Job job;
    Context context;
    ListView list, list_head, list_num;
    ArrayList<HashMap<String, String>> mylist, mylist_title;
    ListAdapter adapter_title, adapter;
    HashMap<String, String> map1, map2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = this.getApplicationContext();
//        TODO: Get data parsed from login
        Bundle exstra = getIntent().getExtras();
        data = new Data(context, exstra.getString("user"));
        String selectedJob = exstra.getString("selectedJob");
        user = (User) data.getData(User.class);
        list = (ListView) findViewById(R.id.lvContent);
        list_head = (ListView) findViewById(R.id.lvHeader);

        showHistoryJob(user, selectedJob);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddJobActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });
    }

    public void showHistoryJob(User user, String selectedJob) {

        mylist = new ArrayList<HashMap<String, String>>();
        mylist_title = new ArrayList<HashMap<String, String>>();

        /**********Display the headings************/

        map1 = new HashMap<String, String>();
        map1.put("DATE", "Date");
        map1.put("TIME_IN", "In");
        map1.put("TIME_OUT", "Out");
        map1.put("TIME_BREAK", "Break");
        map1.put("TOTAL", "Total");
        mylist_title.add(map1);

        try {
            adapter_title = new SimpleAdapter(this, mylist_title, R.layout.content_history,
                    new String[]{"DATE", "TIME_IN", "TIME_OUT", "TIME_BREAK", "TOTAL"}, new int[]{
                    R.id.txvDate, R.id.txvTimeIn, R.id.txvTimeOut, R.id.txvBreak, R.id.txvTotal});
            list_head.setAdapter(adapter_title);
        } catch (Exception e) {

        }

        /**********Display the contents************/

        for (Job job : user.getJobs()) {
            if (job.getName().equals(selectedJob)) {
                for (WorkDate wd : job.getwDates()) {
                    String timeIn = wd.getTimeIn().substring(wd.getTimeIn().indexOf(" "), wd.getTimeIn().length());
                    String timeOut = wd.getTimeOut().substring(wd.getTimeOut().indexOf(" "), wd.getTimeOut().length());
                    map2 = new HashMap<String, String>();
                    map2.put("DATE", wd.getwDate());
                    map2.put("TIME_IN", timeIn);
                    map2.put("TIME_OUT", timeOut);
                    map2.put("TIME_BREAK", wd.getBreakTime());
                    map2.put("TOTAL", Float.toString(job.calwHours()));
                    mylist.add(map2);
                }
            }


        }

        try {
            adapter = new SimpleAdapter(this, mylist, R.layout.content_history,
                    new String[]{"DATE", "TIME_IN", "TIME_OUT", "TIME_BREAK", "TOTAL"}, new int[]{
                    R.id.txvDate, R.id.txvTimeIn, R.id.txvTimeOut, R.id.txvBreak, R.id.txvTotal});
            list.setAdapter(adapter);
        } catch (Exception e) {

        }
    }
}
