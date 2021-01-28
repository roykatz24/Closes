package com.example.closes.PagesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.closes.UtilsPackage.EmailPasswordValidator;
import com.example.closes.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "closes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        initListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(this, CheckDonationsActivity.class));
                finish();
            }
        };
    }

    private void initListeners() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (!EmailPasswordValidator.getInstance().isValidEmail(email) && !EmailPasswordValidator.getInstance().isValidPassword(password)) {
            etEmail.setError("The email is invalid");
            etPassword.setError("The password is invalid");
            etEmail.requestFocus();
            etPassword.requestFocus();
        } else if (!EmailPasswordValidator.getInstance().isValidEmail(email)) {
            etEmail.setError("The email is invalid");
            etEmail.requestFocus();
        } else if (!EmailPasswordValidator.getInstance().isValidPassword(password)) {
            etPassword.setError("The password is invalid");
            etPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithEmail:success");

                            startActivity(new Intent(this, CheckDonationsActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "singInWithEmail:Fail");

                            Toast.makeText(LoginActivity.this, "The login failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

}
