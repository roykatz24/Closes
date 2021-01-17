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

import com.example.closes.R;
import com.example.closes.UtilsPackage.EmailPasswordValidator;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "closes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        initListeners();
    }

    private void initUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    private void register() {
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
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "createUserWithEmail:success");

                            startActivity(new Intent(this, CheckDonatesActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(RegisterActivity.this, "The register failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                register();
                break;
            case R.id.tvLogin:
                onBackPressed();
                break;
        }
    }

}
