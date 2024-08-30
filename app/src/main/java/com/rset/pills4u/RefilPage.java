package com.rset.pills4u;

import static com.rset.pills4u.MainScreen.boxes;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.nikartm.button.FitButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RefilPage extends Fragment {
    private String DeviceID = "0";
    private MaterialCardView b1,b2,b3,b4,refilpills,changepills,refreshpills;
    private KProgressHUD loading;
    private int STATUS_CODE = 0;
    private String[] boxecode = {"Box 1", "Box 2", "Box 3", "Box 4"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_refil, container, false);
        loading = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Refreshing Data")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        SharedPreferences sh = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String details = sh.getString("details", "null");
        b1 = v.findViewById(R.id.box1);
        b2 = v.findViewById(R.id.box2);
        b3 = v.findViewById(R.id.box3);
        b4 = v.findViewById(R.id.box4);
        refilpills = v.findViewById(R.id.RefilBtn);
        changepills = v.findViewById(R.id.ChangePillbtn);
        refreshpills = v.findViewById(R.id.Refreshbtn);
        refreshpills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                getData(details);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.abt_box);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView boxhead = dialog.findViewById(R.id.text12);
                TextView name = dialog.findViewById(R.id.pname);
                TextView count = dialog.findViewById(R.id.pcount);
                TextView weight = dialog.findViewById(R.id.pweight);
                boxhead.setText("Box 1");
                BoxModel b = boxes.get(0);
                name.setText("Pill Name: "+b.getBox_pillname());
                count.setText("Pill Count: "+String.valueOf(b.getBox_pillcount()));
                weight.setText("Pill Weight: "+String.valueOf(b.getBox_pillweight())+"g");
                dialog.show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.abt_box);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView boxhead = dialog.findViewById(R.id.text12);
                TextView name = dialog.findViewById(R.id.pname);
                TextView count = dialog.findViewById(R.id.pcount);
                TextView weight = dialog.findViewById(R.id.pweight);
                boxhead.setText("Box 2");
                BoxModel b = boxes.get(1);
                name.setText("Pill Name: "+b.getBox_pillname());
                count.setText("Pill Count: "+String.valueOf(b.getBox_pillcount()));
                weight.setText("Pill Weight: "+String.valueOf(b.getBox_pillweight())+"g");
                dialog.show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.abt_box);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView boxhead = dialog.findViewById(R.id.text12);
                TextView name = dialog.findViewById(R.id.pname);
                TextView count = dialog.findViewById(R.id.pcount);
                TextView weight = dialog.findViewById(R.id.pweight);
                boxhead.setText("Box 3");
                BoxModel b = boxes.get(2);
                name.setText("Pill Name: "+b.getBox_pillname());
                count.setText("Pill Count: "+String.valueOf(b.getBox_pillcount()));
                weight.setText("Pill Weight: "+String.valueOf(b.getBox_pillweight())+"g");
                dialog.show();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.abt_box);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView boxhead = dialog.findViewById(R.id.text12);
                TextView name = dialog.findViewById(R.id.pname);
                TextView count = dialog.findViewById(R.id.pcount);
                TextView weight = dialog.findViewById(R.id.pweight);
                boxhead.setText("Box 4");
                BoxModel b = boxes.get(3);
                name.setText("Pill Name: "+b.getBox_pillname());
                count.setText("Pill Count: "+String.valueOf(b.getBox_pillcount()));
                weight.setText("Pill Weight: "+String.valueOf(b.getBox_pillweight())+"g");
                dialog.show();
            }
        });
        changepills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.changepills);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                SmartMaterialSpinner spin = dialog.findViewById(R.id.pillSelect);
                spin.setItem(Arrays.asList(boxecode));
                TextInputEditText name = dialog.findViewById(R.id.Pillnamet);
                TextInputEditText count = dialog.findViewById(R.id.Pillcountt);
                FitButton apply = dialog.findViewById(R.id.applybtn);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = spin.getSelectedItemPosition();
                        boxes.get(pos).setBox_pillcount(Integer.parseInt(count.getText().toString()));
                        boxes.get(pos).setBox_pillname(name.getText().toString());
                        boxes.get(pos).setBox_pillweight(Float.parseFloat("100.78"));
                        loading.show();
                        ApplyBox();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        refilpills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.refil_pills);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                SmartMaterialSpinner spin = dialog.findViewById(R.id.pillSelect);
                String[] boxe = new String[4];
                for(int i=0; i<boxes.size(); i++){
                    boxe[i] = boxecode[i]+ " ("+boxes.get(i).box_pillname+")";
                }
                spin.setItem(Arrays.asList(boxe));
                TextInputEditText count = dialog.findViewById(R.id.Pillcountt);
                FitButton apply = dialog.findViewById(R.id.applybtn);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = spin.getSelectedItemPosition();
                        boxes.get(pos).setBox_pillcount(boxes.get(pos).getBox_pillcount()+Integer.parseInt(count.getText().toString()));
                        loading.show();
                        ApplyBox();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return v;
    }
    private void ApplyBox(){
        String[] box = new String[4];
        for(int i=0; i<boxes.size(); i++){
            box[i] = boxes.get(i).getBox_pillname() + "-" + boxes.get(i).getBox_pillcount() + "-" + boxes.get(i).getBox_pillweight()+" ";
            Log.e("US",box[i]);
        }
        SharedPreferences sh = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String details = sh.getString("details", "null");
        String[] field = new String[5];
        field[0] = "userid";
        field[1] = "box1";
        field[2] = "box2";
        field[3] = "box3";
        field[4] = "box4";
        String[] data = new String[5];
        data[0] = details+DeviceID;
        data[1] = box[0];
        data[2] = box[1];
        data[3] = box[2];
        data[4] = box[3];
        PutData putData = new PutData("http://127.0.0.1/pills4u/postPills.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.e("RES",result);
                if (result.equals("Failed")) {
                    Toast.makeText(getContext(), "Data Not Present / Server Failed", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Applied", Toast.LENGTH_LONG).show();
                }
                loading.dismiss();
            }
        }
    }
    private void getData(String details) {
        String[] field = new String[1];
        field[0] = "userid";
        String[] data = new String[1];
        data[0] = details+DeviceID;
        PutData putData = new PutData("http://127.0.0.1/pills4u/getPills.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.e("RES",result);
                if (result.equals("Failed")) {
                    Toast.makeText(getContext(), "Data Not Present / Server Failed", Toast.LENGTH_LONG).show();

                }else{
                    String[] datastrip = result.split(":");
                    if(datastrip.length == 4){
                        for(int i=0; i<4; i++){
                            String[] tmp = datastrip[i].split("-");
                            boxes.get(i).setBox_pillname(tmp[0]);
                            boxes.get(i).setBox_pillcount(Integer.parseInt(tmp[1]));
                            boxes.get(i).setBox_pillweight(Float.parseFloat(tmp[2]));
                        }
                    }
                }
                loading.dismiss();
            }
        }
    }


}
