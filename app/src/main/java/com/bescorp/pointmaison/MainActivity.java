package com.bescorp.pointmaison;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplayDate;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    Spinner utilisateur, typeproduit;
    EditText montant;
    TextView tvDate;
    AppCompatButton btn_valider, btn_quitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setCurrentDateOnView();
        addListenerOnButton();
        final ScrollView coordinatorLayout = (ScrollView) findViewById(R.id.main_content);

        btn_quitter=(AppCompatButton)findViewById(R.id.btn_quitter);

        btn_quitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });


        btn_valider = (AppCompatButton) findViewById(R.id.btn_valider);
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilisateur = (Spinner) findViewById(R.id.utilisateur);
                typeproduit = (Spinner) findViewById(R.id.typeproduit);
                montant = (EditText) findViewById(R.id.montant);
                tvDate = (TextView) findViewById(R.id.tvDate);


                if(montant.getText().toString().isEmpty() || utilisateur.getSelectedItem().toString().isEmpty() || typeproduit.getSelectedItem().toString().isEmpty() || tvDate.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Veuillez remplir tous les champs", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    snackbar.show();
                }else{

                    final CharSequence[] items = {"Oui je comfirme", "Non j'annule" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Vous confirmez cette opération ?");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Oui je comfirme")) {
                                setPointData(utilisateur.getSelectedItem().toString(), typeproduit.getSelectedItem().toString(), montant.getText().toString(), tvDate.getText().toString());
                            } else if (items[item].equals("Non j'annule")) {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Opération annulée", Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorRed));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                                snackbar.show();
                            }

                        }
                    });
                    builder.show();

                    //setPointData(utilisateur.getSelectedItem().toString(), typeproduit.getSelectedItem().toString(), montant.getText().toString(), tvDate.getText().toString());
                }

            }
        });
    }


    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        //dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        //dpResult.init(year, month, day, null);

    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1)
                    .append("-").append(year)
                    .append(" "));


        }
    };

    private void setPointData(String who, String what, String amount, String datecreated){

        String url = "http://pvmaison.000webhostapp.com/";

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        APIService apiService = retrofit.create(APIService.class);


        Call<String> jsonCall = apiService.setPointData(who,what,amount,datecreated);
        Log.e("INFO44", jsonCall.request().toString());
        jsonCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response) {
                Log.v("INFO4", response.body());
                if(response.code() == 200 && response.isSuccessful()){
                    ScrollView coordinatorLayout = (ScrollView) findViewById(R.id.main_content);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enregistrement effectué avec succès", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorVert));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    snackbar.show();
                    montant = (EditText) findViewById(R.id.montant);
                    montant.setText("");

                }else{
                    ScrollView coordinatorLayout = (ScrollView) findViewById(R.id.main_content);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Echec..Veuillez réessayer", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("INFO2", t.toString());
            }
        });

    }
}
