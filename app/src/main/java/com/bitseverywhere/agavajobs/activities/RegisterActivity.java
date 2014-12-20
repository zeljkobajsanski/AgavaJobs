package com.bitseverywhere.agavajobs.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.services.HttpService;

import java.util.regex.Pattern;

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener {

    private View registerForm;
    private ProgressBar progressBar;
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
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        registerForm = findViewById(R.id.registerForm);
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
        showProgress(false);
    }

    private void showError(Exception exc) {
        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
        protected void onPreExecute() {
            RegisterActivity.this.showProgress(true);
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
            if (exc != null) {
                RegisterActivity.this.showError(exc);
            } else {
                RegisterActivity.this.registered(integer);
            }
        }
    }




}
