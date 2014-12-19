package com.bitseverywhere.agavajobs.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.services.HttpService;

import java.util.regex.Pattern;

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText ime, prezime, email, lozinka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ime = (EditText) findViewById(R.id.ime);
        prezime = (EditText) findViewById(R.id.prezime);
        email = (EditText) findViewById(R.id.email);
        lozinka = (EditText) findViewById(R.id.loznika);
        Button btn = (Button)findViewById(R.id.btnRegister);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (TextUtils.isEmpty(ime.getText())) {
            ime.setError("Unesite ime");
            valid = false;
        }
        if (TextUtils.isEmpty(prezime.getText())) {
            prezime.setError("Unesite prezime");
            valid = false;
        }
        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Unesite e-mail");
            valid = false;
        }

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email.getText().toString()).matches()) {
            email.setError("Format e-maila nije ispravan");
            valid = false;
        }

        if (TextUtils.isEmpty(lozinka.getText())) {
            lozinka.setError("Unesite lozinku");
            valid = false;
        }
        if (valid) {
            new RegisterTask(ime.getText().toString(), prezime.getText().toString(),
                    email.getText().toString(), lozinka.getText().toString()).execute();
        }
    }

    private void registered(int userId) {
        if (userId > -1) {
            Intent intent = new Intent();
            intent.putExtra("USER_ID", userId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, Integer> {

        private String ime, prezime, email, lozinka;
        private Exception exc;

        private RegisterTask(String ime, String prezime, String email, String lozinka) {
            this.ime = ime;
            this.prezime = prezime;
            this.email = email;
            this.lozinka = lozinka;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                return HttpService.getInstance().registrujNalog(ime, prezime, email, lozinka);
            } catch (Exception e) {
                exc = e;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            RegisterActivity.this.registered(integer);
        }
    }


}
