package com.rset.pills4u;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import static com.rset.pills4u.MainScreen.boxes;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.Calendar;

public class WelcomePage extends Fragment {
    private String[] WeekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private RecyclerView RV;
    private ScheduleAdapter Adapter;
    private ArrayList<ScheduleData> all_data = new ArrayList<>();
    private TextView DayChange;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_home, container, false);
        DayChange = v.findViewById(R.id.day);
        Calendar c = Calendar.getInstance();
        int index = 0*(c.get(Calendar.DAY_OF_WEEK) - 1);
        DayChange.setText(WeekDays[index]);
        RV = v.findViewById(R.id.AllSchedules);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        KProgressHUD loading1 = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading Schedules")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        loading1.show();
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
                Log.e("RES-->",result);
                result = result.replaceAll("amp;","");
                String[] weeks1 = result.split("\\^");
                for(String s: weeks1)
                    Log.e("RES-->",s);
                if (result.equals("Failed")) {
                    Toast.makeText(getContext(), "Data Not Present / Server Failed", Toast.LENGTH_LONG).show();
                    loading1.dismiss();
                    Adapter = new ScheduleAdapter(all_data);
                    RV.setAdapter(Adapter);

                }else{
                    loading1.dismiss();
                    result = result.replaceAll("amp;","");
                    String[] weeks = result.split("\\^");
                    if(!weeks[index].matches(" ")){
                        String[] tmp = weeks[index].split("&");
                        ArrayList<ScheduleData> tmp1 = new ArrayList<>();
                        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
                        String details2 = sharedPreferences2.getString("CheckedDay", "");
                        String details3 = sharedPreferences2.getString("Checked", "");
                        boolean done = false;
                        for(String t: tmp){
                            if(!t.contains(" ")){
                                String[] k = t.split("-");
                                if(!details2.matches(WeekDays[index])&&!done){
                                    SharedPreferences.Editor myEdit = sharedPreferences2.edit();
                                    myEdit.putString("Checked","").apply();
                                    done=true;
                                }else{
                                    if(done)
                                        all_data.add(new ScheduleData(k[0], k[1]));
                                    else
                                        if(!details3.contains(k[0]))
                                            all_data.add(new ScheduleData(k[0], k[1]));
                                }

                            }
                        }
                    }
                    Adapter= new ScheduleAdapter(all_data);
                    Adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemCheck(int position) {
                            AlertDialog.Builder builder = new AlertDialog
                                    .Builder(getContext())
                                    .setMessage("Do you want to mark as done?")
                                    .setTitle("Confirmation")
                                    .setCancelable(false)
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            String details = sharedPreferences.getString("Checked", "");
                                            details += "-" + all_data.get(position).getPillboxID();
                                            myEdit.putString("Checked", details).apply();
                                            myEdit.putString("CheckedDay", WeekDays[index]).apply();
                                            eatPill(all_data.get(position).getTimeSlot());
                                            all_data.remove(position);
                                            Adapter.notifyItemChanged(position);
                                        }
                                    });
                            builder.create().show();
                        }
                    });
                    RV.setAdapter(Adapter);
                }
            }
        }
        return v;
    }
    void eatPill(String pillname){
        for(BoxModel each:boxes){
            if(each.getBox_pillname().matches(pillname)) {
                each.consume();
                return;
            }
        }
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
                delete.setImageResource(R.drawable.ic_check);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != NO_POSITION){
                                listener.onItemCheck(position);
                            }
                        }
                    }
                });
            }
        }
    }
}
