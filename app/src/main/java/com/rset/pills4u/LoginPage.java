package com.rset.pills4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LoginPage extends AppCompatActivity {
    private Button LoginBtn;
    private TextView Signup;
    private TextInputEditText phn,pass;
    private TextInputLayout phn_t,pass_t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        LoginBtn = findViewById(R.id.button4);
        Signup = findViewById(R.id.textView6);
        phn_t = findViewById(R.id.txtUsername);
        phn = findViewById(R.id.inputUsername);
        pass = findViewById(R.id.inputPassword);
        pass_t = findViewById(R.id.txtPassword);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KProgressHUD loading = KProgressHUD.create(LoginPage.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setDetailsLabel("Verifying Data")
                        .setCancellable(false)
                        .setAnimationSpeed(1)
                        .setDimAmount(0.5f);
                String phn1 = phn.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                if(phn1.length()==10&&!pass1.equals("")&&!pass1.equals("")){
                    loading.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            String[] data = new String[2];
                            data[0] = phn1;
                            data[1] = pass1;
                            PutData putData = new PutData("http://127.0.0.1/pills4u/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.e("RES",result);
                                    if (result.equals("Login Success")) {
                                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails",MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("details", String.valueOf(phn1)).apply();
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismiss();
                                                Intent i = new Intent(LoginPage.this, MainScreen.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }, 1000);

                                    }else if(result.equals("Username or Password wrong")){
                                        Toast.makeText(getApplicationContext(), "Username or Password wrong", Toast.LENGTH_SHORT).show();
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loading.dismiss();
                                            }
                                        }, 1000);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Server Failed", Toast.LENGTH_LONG).show();
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
                    if(phn1.equals(""))
                        phn_t.setError("Enter Phone");
                    else
                        if(phn1.length()!=10)
                            phn_t.setError("Enter valid Phone");
                        else
                            phn_t.setError(null);
                    if(pass1.equals(""))
                        pass_t.setError("Enter Phone");
                    else
                        pass_t.setError(null);
                }
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Signup = new Intent(LoginPage.this, SignUp.class);
                startActivity(Signup);
            }
        });
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
}
