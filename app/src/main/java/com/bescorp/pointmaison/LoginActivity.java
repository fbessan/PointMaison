package com.bescorp.pointmaison;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    AppCompatButton btn_valider;
    EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LinearLayout coordinatorLayout = (LinearLayout) findViewById(R.id.main_content);

        btn_valider=(AppCompatButton)findViewById(R.id.btn_valider);

        btn_valider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                code = (EditText) findViewById(R.id.code);

                if(code.getText().toString().isEmpty() || !code.getText().toString().equals("0000")){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Opération annulée", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    snackbar.show();
                }else{
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });



    }

}
