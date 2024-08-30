package com.rset.pills4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainScreen extends AppCompatActivity {
    private SmoothBottomBar bottomBar;
    public static ArrayList<BoxModel> boxes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscr);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentCont, new WelcomePage()).commit();
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if(i == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentCont, new WelcomePage()).addToBackStack("Welcome").commit();
                }else if(i == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentCont, new SchedulePage()).addToBackStack("Schedule").commit();
                }else if(i == 2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentCont, new MissedPage()).addToBackStack("Missed").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentCont, new RefilPage()).addToBackStack("Refil").commit();
                }
                return false;
            }
        });
        getData();
    }
    private long pressedTime;
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
    private void getData() {
        KProgressHUD loading = KProgressHUD.create(getApplicationContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Refreshing Data")
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        SharedPreferences sh = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String details = sh.getString("details", "null");
        String[] field = new String[1];
        field[0] = "userid";
        String[] data = new String[1];
        data[0] = details+"0";
        boxes.add(new BoxModel("B1", 0, 1.0f));
        boxes.add(new BoxModel("B2", 0,1.0f));
        boxes.add(new BoxModel("B3", 0,1.0f));
        boxes.add(new BoxModel("B4", 0,1.0f));
        PutData putData = new PutData("http://127.0.0.1/pills4u/getPills.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.e("RES",result);
                if (!result.equals("Failed")) {
                    String[] datastrip = result.split(":");
                    if(datastrip.length == 4){
                        for(int i=0; i<4; i++){
                            String[] tmp = datastrip[i].split("-");
                            boxes.get(i).setBox_pillname(tmp[0]);
                            boxes.get(i).setBox_pillcount(Integer.parseInt(tmp[1]));
                            boxes.get(i).setBox_pillweight(Float.parseFloat(tmp[2]));
                        }
                    }
                }else
                    Log.e("Da",result);
                loading.dismiss();
            }
        }
    }
}