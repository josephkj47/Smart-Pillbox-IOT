package com.rset.pills4u;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {
    private ImageView backbtn;
    private Button signup;
    private TextInputEditText fn,ln,em,ph,pass;
    private TextInputLayout fn_t,ln_t,em_t,ph_t,pass_t;
    private TextView did;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);
        backbtn = findViewById(R.id.BackBtnIm);
        fn = findViewById(R.id.F_name_txt);
        ln = findViewById(R.id.L_name_txt);
        em = findViewById(R.id.Email_txt);
        ph = findViewById(R.id.Phone_txt);
        pass = findViewById(R.id.Password_txt);
        fn_t = findViewById(R.id.F_name);
        ln_t = findViewById(R.id.L_name);
        em_t = findViewById(R.id.Email);
        ph_t = findViewById(R.id.Phone);
        pass_t = findViewById(R.id.Password);
        did = findViewById(R.id.deviceIDscan);
        signup = findViewById(R.id.buttonsignup);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KProgressHUD loading = KProgressHUD.create(SignUp.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Uploading Data")
                        .setCancellable(false)
                        .setAnimationSpeed(1)
                        .setDimAmount(0.5f);
                String fname1,lname1,email1,phone1,passw,deviceid1;
                fname1 = fn.getText().toString().trim();
                lname1 = ln.getText().toString().trim();
                email1 = em.getText().toString().trim();
                phone1 = ph.getText().toString().trim();
                passw = pass.getText().toString().trim();
                deviceid1 = did.getText().toString().trim();
                if(!fname1.equals("")&&!lname1.equals("")&&!email1.equals("")&&!phone1.equals("")&&phone1.length()==10&&!passw.equals("")&&!deviceid1.equals("")&& Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                    loading.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[6];
                            field[0] = "fname";
                            field[1] = "lname";
                            field[2] = "email";
                            field[3] = "phone";
                            field[4] = "passwd";
                            field[5] = "deviceID";
                            String[] data = new String[6];
                            data[0] = fname1;
                            data[1] = lname1;
                            data[2] = email1;
                            data[3] = phone1;
                            data[4] = passw;
                            data[5] = "0";
                            PutData putData = new PutData("http://127.0.0.1/pills4u/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.e("RES",result);
                                    if (result.equals("Sign Up Success")) {
                                        Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismiss();
                                                finish();
                                            }
                                        }, 1000);

                                    }else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismiss();
                                            }
                                        }, 1000);
                                    }
                                }
                            }
                        }
                    });
                }else{
                    if(fname1.equals(""))
                        fn_t.setError("Enter First Name");
                    else
                        fn_t.setError(null);
                    if(lname1.equals(""))
                        ln_t.setError("Enter Last Name");
                    else
                        ln_t.setError(null);
                    if(email1.equals(""))
                       em_t.setError("Enter Email");
                    else
                        if(Patterns.EMAIL_ADDRESS.matcher(email1).matches())
                            em_t.setError("Enter Valid Email");
                        else
                            em_t.setError(null);
                    if(phone1.equals(""))
                        ph_t.setError("Enter Phone");
                    else
                        if(phone1.length()!=10)
                            ph_t.setError("Enter Valid Phone");
                        else
                            ph_t.setError(null);
                    if(passw.equals(""))
                        pass_t.setError("Enter Password");
                    else
                        pass_t.setError(null);
                }
            }
        });
    }
}
