package com.rset.pills4u;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MissedPage extends Fragment {
    private String[] WeekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    private RecyclerView RV;
    private ScheduleAdapter Adapter;
    private ArrayList<ScheduleData> all_data = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_missed, container, false);
        RV = v.findViewById(R.id.Missed);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        Calendar c = Calendar.getInstance();
        int index = c.get(Calendar.DAY_OF_WEEK) - 1;
        SharedPreferences sh = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String details = sh.getString("details", "null");
        String[] field = new String[1];
        field[0] = "phone";
        String[] data = new String[1];
        data[0] = details;
        PutData putData = new PutData("http://127.0.0.1/pills4u/getUser.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.e("RES",result);
                if (result.equals("Failed")) {
                    Toast.makeText(getContext(), "Data Not Present / Server Failed", Toast.LENGTH_LONG).show();
                    Adapter = new ScheduleAdapter(all_data);
                    RV.setAdapter(Adapter);

                }else{
                    result = result.replaceAll("amp;","");
                    String[] weeks = result.split("\\^");
                    if(!weeks[index].matches(" ")){
                        String[] tmp = weeks[index].split("&");
                        ArrayList<ScheduleData> tmp1 = new ArrayList<>();
                        tmp1.clear();
                        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
                        String details2 = sharedPreferences2.getString("CheckedDay", "");
                        String details3 = sharedPreferences2.getString("Checked", "");
                        boolean done = false;
                        for(String t: tmp){
                            if(!t.matches(" ")){
                                String[] k = t.split("-");
                                if(!details2.matches(WeekDays[index])&&!done){
                                    SharedPreferences.Editor myEdit = sharedPreferences2.edit();
                                    myEdit.putString("Checked","").apply();
                                    done=true;
                                }else{
                                    if(done)
                                        all_data.add(new ScheduleData(k[0], k[1]));
                                    else
                                    if(!details3.contains(k[0])){
                                        SimpleDateFormat of = new SimpleDateFormat("hh:mm aa");
                                        Date db = null;
                                        try {
                                            db = of.parse(k[0].trim());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Date curr = c.getTime();
                                        if(curr.after(db))
                                            all_data.add(new ScheduleData(k[0], k[1]));
                                    }

                                }

                            }
                        }
                    }
                    Adapter= new ScheduleAdapter(all_data);
                    RV.setAdapter(Adapter);
                }
            }
        }
        return v;
    }
    public static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.viewHolder> {

        ArrayList<ScheduleData> schedules;
        public ScheduleAdapter(ArrayList<ScheduleData> schedule) {
            this.schedules = schedule;
        }
        private ScheduleAdapter.OnItemClickListener mlistener;
        public interface OnItemClickListener{
            void onItemCheck(int position);
        }
        public void setOnItemClickListener(ScheduleAdapter.OnItemClickListener listener){
            mlistener = listener;
        }
        @NonNull
        @Override
        public ScheduleAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_model, parent, false);
            return new ScheduleAdapter.viewHolder(v,mlistener);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleAdapter.viewHolder holder, int position) {
            ScheduleData current = schedules.get(position);
            if(current != null){
                holder.Pill_name.setText(current.getPillboxID());
                holder.Pill_time.setText(current.getTimeSlot());
            }

        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder{
            private TextView Pill_time, Pill_name;
            private ImageView delete;
            public viewHolder(@NonNull View itemView, OnItemClickListener listener) {
                super(itemView);
                Pill_time = itemView.findViewById(R.id.pill_time);
                Pill_name = itemView.findViewById(R.id.pill_name);
                delete = itemView.findViewById(R.id.delete_btn);
                delete.setImageResource(R.drawable.ic_missed);
            }
        }
    }
}