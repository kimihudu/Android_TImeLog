package dev.android.timelog;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static dev.android.timelog.Job.LONG_FORMAT;
//import java.util.Calendar;

/**
 * Created by GeorgyGeo on 6/19/2017.
 */

public class Job {

    private String name;
    private float rate;
    private ArrayList<WorkDate> wDates = new ArrayList();
    private float wHours;
    private float salary = 0;
    private String address;
    private String des;
    private String phone;
    public static final String LONG_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public Job() {
    }

    public Job(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public Job(String name, float rate, ArrayList<WorkDate> wDates, long wHours, float salary) {
        this.name = name;
        this.rate = rate;
        this.wDates = wDates;
        this.wHours = wHours;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null)
            this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null)
            this.address = address;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public ArrayList<WorkDate> getwDates() {
        return wDates;
    }

    public void setwDates(ArrayList<WorkDate> wDates) {
        if (wDates != null)
            this.wDates = wDates;
    }

    public float getwHours() {
        return wHours;
    }

    public void setwHours(float wHours) {
        this.wHours = wHours;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public float calSalary() {
        return calwHours() * rate;
    }

    public float calwHours() {
        float total = 0;
        for (WorkDate wdate : wDates) {
            total += wdate.period();
        }
        return total;
    }


}

// TODO: define workdate class
class WorkDate {
    private String wDate = null;
    private String timeIn = null;
    private String timeOut = null;
    private String breakTime = null;

    public WorkDate() {
    }

    public WorkDate(DateTime wDate, DateTime timeIn, DateTime timeOut) {
        this.wDate = time2String(wDate);
        this.timeIn = time2String(timeIn);
        this.timeOut = time2String(timeOut);
    }

    public String getwDate() {
        return wDate;
    }

    public void setwDate(DateTime wDate) {
        if (wDate != null)
            this.wDate = time2String(wDate);
    }

    public void setwDate(String wDate) {
        if (wDate != null)
            this.wDate = wDate;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(DateTime timeIn) {
        if (timeIn != null)
            this.timeIn = time2String(timeIn);
    }

    public void setTimeIn(String timeIn) {
        if (timeIn != null)
            this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(DateTime timeOut) {
        if (timeOut != null)
            this.timeOut = time2String(timeOut);
    }

    public void setTimeOut(String timeOut) {
        if (timeOut != null)
            this.timeOut = timeOut;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public float period() {

        Period duration = new Period(string2Time(timeIn), string2Time(timeOut));
        float _breakTime = Long.parseLong(breakTime);
        float totalHours = duration.getHours() - _breakTime / 60;
        return totalHours;
    }

    private DateTime string2Time(String str) {

        DateTime time = DateTime.parse(str,
                DateTimeFormat.forPattern(LONG_FORMAT));
        return time;
    }

    private String time2String(DateTime time) {
        DateTimeFormatter ft = DateTimeFormat.forPattern(LONG_FORMAT);
        return ft.print(time);
    }


    //    TODO: add new workdate
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

    //    TODO: edit workdate
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

    //    TODO: delete workdate
    public boolean deleteWD(Context context, String userName, String job, String wd) {
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
}
