package dev.android.timelog;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
//import com.example.georgygeo.myapplication.Job;

/**
 * Created by GeorgyGeo on 6/19/2017.
 */

public class User {


    private String usrName;
    private String usrPass;
    private ArrayList<Job> jobs = new ArrayList<Job>();
    private float income;
    private long totalHours;

    public User() {
    }

    public User(String usrName, String usrPass) {
        this.usrName = usrName;
        this.usrPass = usrPass;
    }

    public User(String usrName, String usrPass, ArrayList jobs, float income) {
        this.usrName = usrName;
        this.usrPass = usrPass;
        this.jobs = jobs;
        this.income = income;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrPass() {
        return usrPass;
    }

    public void setUsrPass(String usrPass) {
        this.usrPass = usrPass;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList jobs) {
        this.jobs = jobs;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }

    //    TODO: calculate income base on total salary list jobs
    private float calIncome() {
        float total = 0;
        for (Job job : jobs) {
            total += job.getSalary();
        }
        return total;
    }

    //    TODO: calculate income base on total salary list jobs
    private long calTotalHours() {
        long total = 0;
        for (Job job : jobs) {
            total += job.getwHours();
        }
        return total;
    }

    //    TODO: add new job
    public boolean addJob(Context context, String userName, Job newJob) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);
        user.getJobs().add(newJob);
        user.setTotalHours(user.calTotalHours());
        user.setIncome(user.calIncome());

        return data.setData(user);
    }

    //    TODO: edit selected job
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
    public boolean editJob(Context context, String userName,String selectedJob, Job jobUpdate) {

        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        for (Job editJob : user.getJobs()) {
            if (editJob.getName().equals(selectedJob)) {
                editJob.setName(jobUpdate.getName());
                editJob.setRate(jobUpdate.getRate());
                editJob.setAddress(jobUpdate.getAddress());
                editJob.setwDates(jobUpdate.getwDates());
                editJob.setwHours(editJob.calwHours());
                editJob.setSalary(editJob.calSalary());

                user.setTotalHours(user.calTotalHours());
                user.setIncome(user.calIncome());
                return data.setData(user);
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
        return false;
    }

    //    TODO: delete selected job
    public boolean deleteJob(Context context, String userName, String job) {

        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);
        for (Job delJob : user.getJobs()) {
            if (delJob.getName().equals(job)) {
                user.getJobs().remove(delJob);
                Log.i("DELETE",delJob.getName());
                Toast.makeText(context, "deleted " + delJob.getName(), Toast.LENGTH_LONG).show();
                return data.setData(user);
            }
        }
        return false;
    }

    //    TODO: Edit user
    public void editUser(Context context, String userName, User userUpdate) {
        Data data = new Data(context, userName);
        User user = (User) data.getData(User.class);

        user.setUsrName(userUpdate.getUsrName());
        user.setUsrPass(userUpdate.getUsrPass());
        user.setJobs(userUpdate.getJobs());

        data.setData(user);
    }
}
