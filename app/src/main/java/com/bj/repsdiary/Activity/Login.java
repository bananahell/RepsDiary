package com.bj.repsdiary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bj.repsdiary.R;
import com.bj.repsdiary.Utils.FirebaseHandling;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

  private EditText mEmail;
  private EditText mPassword;
  private CheckBox mVisibility;
  private Button mLogin;
  private ProgressBar mProgressBarLogin;
  private Button mRegister;
  private FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mEmail = findViewById(R.id.login_edittext_email);
    mPassword = findViewById(R.id.login_edittext_password);
    mVisibility = findViewById(R.id.login_checkbox_visibility);
    mLogin = findViewById(R.id.login_button_login);
    mRegister = findViewById(R.id.login_button_register);
    mProgressBarLogin = findViewById(R.id.login_progressBar_login);

    mFirebaseAuth = FirebaseAuth.getInstance();

    mVisibility.setOnCheckedChangeListener(this::setPasswordVisibility);
    mLogin.setOnClickListener(v -> tryToSignInWithEmailAndPassword());
    mRegister.setOnClickListener(v -> goToRegisterScreen());
  }

  private void setPasswordVisibility(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
      mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    } else {
      mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
  }

  private void tryToSignInWithEmailAndPassword() {
    String email = mEmail.getText().toString();
    String password = mPassword.getText().toString();
    if (!email.isEmpty() && !password.isEmpty()) {
      mProgressBarLogin.setVisibility(View.VISIBLE);
      mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          goToMainScreen();
        } else {
          Toast.makeText(Login.this, FirebaseHandling.errorHandling(getApplicationContext(), task.getException()), Toast.LENGTH_LONG).show();
        }
      });
      mProgressBarLogin.setVisibility(View.INVISIBLE);
    } else {
      Toast.makeText(Login.this, R.string.all_fields_necessary, Toast.LENGTH_LONG).show();
    }
  }

  private void goToMainScreen() {
    startActivity(new Intent(Login.this, MainActivity.class));
    finish();
  }

  private void goToRegisterScreen() {
    startActivity(new Intent(Login.this, Register.class));
    finish();
  }

}
