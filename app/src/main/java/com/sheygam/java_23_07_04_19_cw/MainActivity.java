package com.sheygam.java_23_07_04_19_cw;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputEmail, inputPassword;
    Button regBtn, loginBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        regBtn = findViewById(R.id.regBtn);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        regBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.regBtn){
            new RegTask().execute();
        }
    }

    class RegTask extends AsyncTask<Void,Void,String>{
        String email,password;
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            inputEmail.setEnabled(false);
            inputPassword.setEnabled(false);
            regBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "Registration ok";
            try {
                String token = HttpProvider.getInstance()
                        .registration(email,password);
                //Todo save token to SharedPreferences
                Log.d("MainActivity", "doInBackground token: " + token);
            } catch (RuntimeException ex){
                isSuccess = false;
                result = ex.getMessage();
            }catch (IOException e) {
                e.printStackTrace();
                result = "Connection error!Check your internet!";
                isSuccess = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            inputPassword.setEnabled(true);
            inputEmail.setEnabled(true);
            loginBtn.setEnabled(true);
            regBtn.setEnabled(true);
            if(isSuccess){
                //Todo show next Activity
            }else{
                //Todo show error dialog
            }
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
