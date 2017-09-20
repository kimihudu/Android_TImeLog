package dev.android.timelog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.opendevl.JFlat;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SummaryActivity extends AppCompatActivity {
    Data data;
    User user;
    Job job;
    Context context;
    ListView list, list_head, list_num;
    ArrayList<HashMap<String, String>> mylist, mylist_title;
    ListAdapter adapter_title, adapter;
    HashMap<String, String> map1, map2;
    String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        context = this.getApplicationContext();

//                      TODO: Get data parsed from login
        Bundle exstra = getIntent().getExtras();
        userEmail = exstra.getString("user");
        data = new Data(context, exstra.getString("user"));
        user = (User) data.getData(User.class);
        list = (ListView) findViewById(R.id.lvContent);
        list_head = (ListView) findViewById(R.id.lvHeader);
//        list_num = (ListView) findViewById(R.id.listView3);

        showJobs(user);
        Button btnExport = (Button) findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCSV2Mail(context, userEmail, user);
            }
        });

        Button btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtra("user", user.getUsrName());
                startActivity(i);
            }
        });
    }

    public void showJobs(User user) {

        mylist = new ArrayList<HashMap<String, String>>();
        mylist_title = new ArrayList<HashMap<String, String>>();

        /**********Display the headings************/

        map1 = new HashMap<String, String>();
        map1.put("txJobTitle", "Job");
        map1.put("txTotalHours", "WorkHours");
        map1.put("txTotalSalary", "Salary");
        mylist_title.add(map1);

        try {
            adapter_title = new SimpleAdapter(this, mylist_title, R.layout.content_list_summary,
                    new String[]{"txJobTitle", "txTotalHours", "txTotalSalary"}, new int[]{
                    R.id.txJobTitle, R.id.txTotalHours, R.id.txTotalSalary});
            list_head.setAdapter(adapter_title);
        } catch (Exception e) {

        }

        /**********Display the contents************/

        for (Job job : user.getJobs()) {
            map2 = new HashMap<String, String>();

            map2.put("txJobTitle", job.getName());
            map2.put("txTotalHours", Float.toString(job.getwHours()));
            map2.put("txTotalSalary", Float.toString(job.getSalary()));
            mylist.add(map2);
        }

        try {
            adapter = new SimpleAdapter(this, mylist, R.layout.content_list_summary,
                    new String[]{"txJobTitle", "txTotalHours", "txTotalSalary"}, new int[]{
                    R.id.txJobTitle, R.id.txTotalHours, R.id.txTotalSalary});
            list.setAdapter(adapter);
        } catch (Exception e) {

        }
    }

    //          TODO: convert obj data to csv then send mail
    private void sendCSV2Mail(Context context, String userName, Object data) {

//TODO:       convert obj to csv
        File path = context.getExternalFilesDir(null);
        String filePath = path.getAbsolutePath() + "/" + userName + ".csv";
        Gson gson = new Gson();
        String jsonContent = gson.toJson(data);
        JFlat flatMe = new JFlat(jsonContent);

//directly write the JSON document to CSV
        try {
            flatMe.json2Sheet().write2csv(filePath);
        } catch (Exception e) {
            Log.i("Export to CSV", e.toString());
        }

//TODO:        send mail with attachment
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            String message = "File to be attached is " + userName ;
            intent.putExtra(Intent.EXTRA_SUBJECT, "[TimeLog]["+ userName +"]Summary");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setData(Uri.parse("mailto:" + userName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch (Exception e) {
            Log.e("SEND_MAIL","cannot send mail");
        }
    }


}
