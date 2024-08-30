package com.rset.pills4u;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import static com.rset.pills4u.MainScreen.boxes;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.nikartm.button.FitButton;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SchedulePage extends Fragment {
    private SmartMaterialSpinner<String> selectWeek;
    private String[] WeekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String[] pill_list = new String[boxes.size()];
    private FitButton apply,add;
    private RecyclerView RV;
    private ArrayList<ScheduleAdapter> Adapter = new ArrayList<>();
    private ArrayList<ArrayList<ScheduleData>> all_data = new ArrayList<>();
    private String time_now;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_schedule, container, false);
        selectWeek = v.findViewById(R.id.WeekSelector);
        selectWeek.setItem(Arrays.asList(WeekDays));
        apply = v.findViewById(R.id.applybtn);
        add = v.findViewById(R.id.addbtn);
        RV = v.findViewById(R.id.AllSchedules);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        for(int i=0; i<boxes.size(); i++){
            pill_list[i] = boxes.get(i).getBox_pillname();
        }
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
                Log.e("RES",result);
                if (result.equals("Failed")) {
                    Toast.makeText(getContext(), "Data Not Present / Server Failed", Toast.LENGTH_LONG).show();
                    loading1.dismiss();
                    for(int i=0; i<7; i++){
                        all_data.add(new ArrayList<>());
                        Adapter.add(new ScheduleAdapter(all_data.get(i)));
                    }
                    selectWeek.setSelection(0);
                    RV.setAdapter(Adapter.get(0));
                    init_adapter_deleter();

                }else{
                    loading1.dismiss();
                    result = result.replaceAll("amp;","");
                    String[] weeks = result.split("\\^");
                    for(int i=0; i<7; i++){
                        if(!weeks[i].matches(" ")){
                            String[] tmp = weeks[i].split("&");
                            ArrayList<ScheduleData> tmp1 = new ArrayList<>();
                            tmp1.clear();
                            for(String t: tmp){
                                if(!t.matches(" ")){
                                    String[] k = t.split("-");
                                    tmp1.add(new ScheduleData(k[0], k[1]));
                                }
                            }
                            all_data.add(new ArrayList<>(tmp1));
                            Adapter.add(new ScheduleAdapter(all_data.get(i)));
                        }else{
                            all_data.add(new ArrayList<>());
                            Adapter.add(new ScheduleAdapter(all_data.get(i)));
                        }
                    }
                    selectWeek.setSelection(0);
                    RV.setAdapter(Adapter.get(0));
                    init_adapter_deleter();
                }
            }
        }
        selectWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        RV.setAdapter(Adapter.get(0));
                        return;
                    case 1:
                        RV.setAdapter(Adapter.get(1));
                        return;
                    case 2:
                        RV.setAdapter(Adapter.get(2));
                        return;
                    case 3:
                        RV.setAdapter(Adapter.get(3));
                        return;
                    case 4:
                        RV.setAdapter(Adapter.get(4));
                        return;
                    case 5:
                        RV.setAdapter(Adapter.get(5));
                        return;
                    case 6:
                        RV.setAdapter(Adapter.get(6));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_schedule);
                FitButton conf = dialog.findViewById(R.id.applybtn);
                SmartMaterialSpinner<String> pills = dialog.findViewById(R.id.PillSelector);
                RelativeLayout time = dialog.findViewById(R.id.datess);
                TextView tv = dialog.findViewById(R.id.SetTime);
                pills.setItem(Arrays.asList(pill_list));
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time_now = hourOfDay + ":" + minute;
                                SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                                SimpleDateFormat of = new SimpleDateFormat("hh:mm aa");
                                Date dateCode12 = null;
                                try {
                                    dateCode12 = df.parse(time_now);
                                    time_now = of.format(dateCode12);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv.setText(time_now);
                            }
                        }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });

                conf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int current_id = selectWeek.getSelectedItemPosition();
                        all_data.get(current_id).add(new ScheduleData(time_now, pills.getSelectedItem()));
                        Adapter.get(current_id).notifyItemChanged(all_data.get(current_id).size() - 1);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyChanges(all_data);
            }
        });

        return v;
    }

    private void init_adapter_deleter() {
        Adapter.get(0).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(0).remove(position);
                Adapter.get(0).notifyItemChanged(position);
            }
        });
        Adapter.get(1).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(1).remove(position);
                Adapter.get(1).notifyItemChanged(position);
            }
        });
        Adapter.get(2).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(2).remove(position);
                Adapter.get(2).notifyItemChanged(position);
            }
        });
        Adapter.get(3).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(3).remove(position);
                Adapter.get(3).notifyItemChanged(position);
            }
        });
        Adapter.get(4).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(4).remove(position);
                Adapter.get(4).notifyItemChanged(position);
            }
        });
        Adapter.get(5).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(5).remove(position);
                Adapter.get(5).notifyItemChanged(position);
            }
        });
        Adapter.get(6).setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                all_data.get(6).remove(position);
                Adapter.get(6).notifyItemChanged(position);
            }
        });
    }

    private boolean ApplyChanges(ArrayList<ArrayList<ScheduleData>> arrayList){
        StringBuilder str = new StringBuilder();
        for(ArrayList<ScheduleData> each: arrayList){
            for(ScheduleData each1: each)
                str.append(each1.getPillboxID()).append("-").append(each1.getTimeSlot()).append("&");
            str.append(" ^");
        }
        SharedPreferences sh = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String details = sh.getString("details", "null");
        KProgressHUD loading = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Uploading Data")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        loading.show();
        String[] field = new String[2];
        field[0] = "phone";
        field[1] = "schedules";
        String[] data = new String[2];
        data[0] = details;
        data[1] = str.toString();
        PutData putData = new PutData("http://127.0.0.1/pills4u/addSchedule.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.e("RES",result);
                if (result.contains("Done")) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismiss();
                            Toast.makeText(getContext(), "Applied Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);

                }else if(result.equals("Username or Password wrong")){
                    Toast.makeText(getContext(),"Username or Password wrong",Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismiss();
                        }
                    }, 1000);
                }else{
                    Toast.makeText(getContext(),"Server Failed",Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismiss();
                        }
                    }, 1000);
                }
            }
        }
        return true;
    }
    public static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.viewHolder> {

        ArrayList<ScheduleData> schedules;
        private OnItemClickListener mlistener;
        public ScheduleAdapter(ArrayList<ScheduleData> schedule) {
            this.schedules = schedule;
        }
        public interface OnItemClickListener{
            void onItemDelete(int position);
        }
        public void setOnItemClickListener(OnItemClickListener listener){
            mlistener = listener;
        }
        @NonNull
        @Override
        public ScheduleAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_model, parent, false);
            return new viewHolder(v,mlistener);
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

        public static class viewHolder extends RecyclerView.ViewHolder{
            private TextView Pill_time, Pill_name;
            private ImageView delete;
            public viewHolder(@NonNull View itemView, OnItemClickListener listener) {
                super(itemView);
                Pill_time = itemView.findViewById(R.id.pill_time);
                Pill_name = itemView.findViewById(R.id.pill_name);
                delete = itemView.findViewById(R.id.delete_btn);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != NO_POSITION){
                                listener.onItemDelete(position);
                            }
                        }
                    }
                });
            }
        }
    }
}

