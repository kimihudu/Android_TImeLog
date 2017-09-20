package dev.android.timelog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView timer;
    private TextView info;
    private TextView total;
    private DateTime timeIn;
    private DateTime timeOut;
    DateTimeFormatter sdf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
    User user;
    //    Job jobs;
    Data data;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

//        TODO: get user from login, add joblist
        Bundle exstra = getIntent().getExtras();
        data = new Data(context, exstra.getString("user"));
        user = (User) data.getData(User.class);

//        ArrayList listJob = new ArrayList();
//        listJob.add(new Job("dev", 15));
//        listJob.add(new Job("tester", 10));
//        listJob.add(new Job("designer", 5));

//        user.setJobs(listJob);
        List<String> tmp = new ArrayList();
        tmp.add(user.getJobs().get(0).getName());
        tmp.add(user.getJobs().get(1).getName());
        tmp.add(user.getJobs().get(2).getName());
//
        Spinner spinner = (Spinner) findViewById(R.id.jobList);
        ArrayAdapter<Job> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tmp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        info = (TextView) findViewById(R.id.info);
        info.setText(user.getUsrName());
        total = (TextView) findViewById(R.id.total);
        timer = (TextView) findViewById(R.id.timer);

        Button punchIn = (Button) findViewById(R.id.punchIn);
        punchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeIn = new DateTime();
                timer.setText(sdf.print(timeIn));
            }
        });

        Button punchOut = (Button) findViewById(R.id.punchOut);
        punchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeOut = new DateTime();
                timeOut = timeOut.plusHours(4);
//                Job job1 = user.getJobs().get(0);

                DateTime date = DateTime.parse("27/06/2017",
                        DateTimeFormat.forPattern("dd/MM/yyyy"));
                DateTime timeIn = DateTime.parse("27/06/2017 9:27:05",
                        DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
                DateTime timeOut = DateTime.parse("27/06/2017 12:27:05",
                        DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));

                WorkDate updateWD = new WorkDate();
                updateWD.setwDate("28/06/2017");
                updateWD.setTimeOut(timeOut);
                updateWD.setTimeIn(timeIn);

                Job newJob = new Job("php", 15);
                newJob.setAddress("test");
/*
                editWD(context, user.getUsrName(), "tester", "06/27/2017", updateWD);
                addWD(context,user.getUsrName(),"designer",updateWD);
                addJob(context,user.getUsrName(),newJob);
                boolean tmp = editJob(context,user.getUsrName(),newJob);
                deleteJob(context,user.getUsrName(),"php");
                boolean tmp = deleteWD(context,user.getUsrName(),"dev","27/06/2017 00:00:00");
*/

//  calIncome();

//                ArrayList wdate = new ArrayList();
//                wdate.add(new WorkDate(date,timeIn,timeOut));
//                wdate.add(new WorkDate(date,timeIn,timeOut));
//                wdate.add(new WorkDate(date,timeIn,timeOut));
//                job1.setwDates(wdate);


//                Toast.makeText(MainActivity.this, "save data...", Toast.LENGTH_LONG).show();
//                job1.setProfix(job1.calProfix());
//                job2.setProfix(job2.calProfix());
//                user.setSalary(user.calSalary());
//                data.setData(user);
                //total.setText(String.valueOf(user.calSalary()));
            }
        });

        //total.setText(String.valueOf(user.calSalary()));
    }

    private long calIncome() {
        long income = 0;
        for (Job job : user.getJobs()) {
            income += job.calSalary();
        }
        return income;
//        TextView totalView = (TextView) findViewById(R.id.total);
//        TextView wHoursView = (TextView) findViewById(R.id.hours);
//        totalView.setText(String.valueOf(income));
//        wHoursView.setText(String.valueOf(wHours));
    }

    private long calWH() {
        long wHours = 0;
        for (Job job : user.getJobs()) {
            wHours += job.calwHours();
        }
        return wHours;
    }

    private boolean addJob(Context context, String userName, Job newJob) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);
        user.getJobs().add(newJob);
        return data.setData(user);
    }

    /*
        * param:
        * array info to update : String info
        * info = '{
        *   "name": "dev",
          "rate": 15.0,
          "salary": 0.0,
          "wDates": [
                    {
                      "timeIn": "04/02/2011 20:27:05",
                      " timeOut": "04/02/2011 22:27:05",
                      "wDate": "04/02/2011 00:00:00"
                    }],
            "wHours": 0}'
        * */
    private boolean editJob(Context context, String userName, Job jobUpdate) {

        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        for (Job editJob : user.getJobs()) {
            if (editJob.getName() == jobUpdate.getName()) {
                editJob.setName(jobUpdate.getName());
                editJob.setRate(jobUpdate.getRate());
                editJob.setAddress(jobUpdate.getAddress());
                editJob.setwDates(jobUpdate.getwDates());
            /*    for (WorkDate editWD : editJob.getwDates()) {
                    for (WorkDate jobUpdateWD : jobUpdate.getwDates()) {
                        if (editWD.getwDate() == jobUpdateWD.getwDate()) {
                            editWD.setwDate(jobUpdateWD.getwDate());
                            editWD.setTimeIn(jobUpdateWD.getTimeIn());
                            editWD.setTimeOut(jobUpdateWD.getTimeOut());
                        }
                    }
                }
            */
            }
        }
        return data.setData(user);
    }

    private boolean addWD(Context context, String userName, String job, WorkDate newWD) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        for (Job seletedJob : user.getJobs()) {
            if (seletedJob.getName().equals(job)) {
                seletedJob.getwDates().add(newWD);
            }
        }
        Log.i("ADDWD", "added workdate " + data.setData(user));
        return data.setData(user);
    }

    /*
    * update workDate for selected job follow structured info
    * info{
    *   wDate: string,
    *   timeIn: string,
    *   timeOut: string
    * }
    */
    private boolean editWD(Context context, String userName, String job, String workDate, WorkDate updateWD) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        for (Job seletedJob : user.getJobs()) {
            if (seletedJob.getName().equals(job)) {
                for (WorkDate selectedWD : seletedJob.getwDates()) {
                    if (selectedWD.getwDate().equals(workDate)) {
                        selectedWD.setwDate(updateWD.getwDate());
                        selectedWD.setTimeIn(updateWD.getTimeIn());
                        selectedWD.setTimeOut(updateWD.getTimeOut());
                    }
                }
            }
        }
        Log.i("UPDATEWD", "updated " + data.setData(user));
        return data.setData(user);
    }

    /*
    * edit user follow structured info
     *info{
     *      usrName: String,
     *      usrPass: String,
     *      jobs:[]
     * }
    *
    * */

    private void editUser(Context context, String userName, User userUpdate) {
        Data data = new Data(context,userName);
        User user = (User)data.getData(User.class);

        user.setUsrName(userUpdate.getUsrName());
        user.setUsrPass(userUpdate.getUsrPass());
        user.setJobs(userUpdate.getJobs());

        data.setData(user);
    }

    /*
    * get user template for string info
    * */
    private String getUser2String(String userName) {
        Gson gson = new Gson();
        Data dt = new Data(context, userName);
        User template = (User) dt.getData(User.class);
        return gson.toJson(template);
    }

    /*
    * get job template for string info
    * */
    private String getJob2String(String jobName) {
        Gson gson = new Gson();
        for (Job selected : user.getJobs()) {
            if (selected.getName() == jobName)
                return gson.toJson(selected);
        }
        return null;
    }


    private boolean deleteJob(Context context, String userName, String job) {

        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);
        for (Job delJob : user.getJobs()) {
            if (delJob.getName().equals(job)) {
                user.getJobs().remove(delJob);
            }
        }
        return data.setData(user);
    }

    private boolean deleteWD(Context context, String userName, String job, String wd) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        for (Job selectedJob : user.getJobs()) {
            if (selectedJob.getName().equals(job)) {
                for (WorkDate delWD : selectedJob.getwDates()) {
                    if (delWD.getwDate().equals(wd))
                        selectedJob.getwDates().remove(delWD);
                }
            }
        }
        return data.setData(user);
    }

    private void parseData2Listview(String lvID, User user) {
        int _lvID = getResources().getIdentifier(lvID,"id",context.getPackageName());
        ListView lvControl = (ListView) findViewById(_lvID);
        ArrayList<HashMap<String, Job>> jobList;
        for(Job job : user.getJobs()){


        }

    }

}
