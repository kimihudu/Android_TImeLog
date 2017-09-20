package dev.android.timelog;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.RunnableScheduledFuture;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ListJobAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
//    EditText txtJobName;
//    EditText txtJobRate;
//    EditText txtDes;
//    EditText txtBreak;
//    EditText txtPhone;
//    EditText txtAddress;
//    Button btnDelete;
//    Button btnPunchIn;
    Button btnPunchOut;
    TextView txvJobName;
    TextView txvJobRate;
    TextView txvJobLoc;
    Button btnDelete;


    public ListJobAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.content_list_jobs, null);
        }

//        txtJobName = (EditText) convertView.findViewById(R.id.txtTitle);
//        txtJobRate = (EditText) convertView.findViewById(R.id.txtWage);
//        txtAddress = (EditText) convertView.findViewById(R.id.txtAddress);
//        txtBreak = (EditText) convertView.findViewById(R.id.txtBreak);
//        txtPhone = (EditText) convertView.findViewById(R.id.txtPhone);
//        txtDes = (EditText) convertView.findViewById(R.id.txtDes);
//        btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
//        btnPunchIn = (Button)convertView.findViewById(R.id.btnPunchIn);
        btnPunchOut = (Button)convertView.findViewById(R.id.btnPunchOut);

        txvJobName = (TextView) convertView.findViewById(R.id.txvJobTitle);
        txvJobRate = (TextView) convertView.findViewById(R.id.txvJobRate);
        txvJobLoc = (TextView) convertView.findViewById(R.id.txvJobLoc);
        btnDelete = (Button) convertView.findViewById(R.id.btnDelete);


        HashMap<String, String> map = list.get(position);
//        txtJobName.setText(map.get("JOB_NAME"));
//        txtJobRate.setText(map.get("JOB_RATE"));
//        txtAddress.setText(map.get("JOB_LOC"));

        txvJobName.setText(map.get("JOB_NAME"));
        txvJobRate.setText(map.get("JOB_RATE"));
        txvJobLoc.setText(map.get("JOB_LOC"));


        final String selectedJob = map.get("JOB_NAME");
        final String owner = map.get("JOB_OWNER");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              remove from list
                list.remove(position);
                notifyDataSetChanged();

//               remove and update to file
                Context context = v.getContext();
                Data data = new Data(context, owner);
                User user = (User) data.getData(User.class);
                user.deleteJob(context, user.getUsrName(), selectedJob);
            }
        });
        return convertView;
    }


}