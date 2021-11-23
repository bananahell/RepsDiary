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

import com.bj.repsdiary.MainActivity;
import com.bj.repsdiary.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

  private EditText mEmail;
  private EditText mPassword;
  private CheckBox mPasswordVisibility;
  private EditText mConfirmPassword;
  private CheckBox mConfirmPasswordVisibility;
  private Button mRegister;
  private ProgressBar mProgressBarRegister;
  private Button mBack;
  private FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    mEmail = findViewById(R.id.register_edittext_email);
    mPassword = findViewById(R.id.register_edittext_password);
    mPasswordVisibility = findViewById(R.id.register_checkbox_passwordVisibility);
    mConfirmPassword = findViewById(R.id.register_edittext_confirmPassword);
    mConfirmPasswordVisibility = findViewById(R.id.register_checkbox_confirmPasswordVisibility);
    mRegister = findViewById(R.id.register_button_register);
    mProgressBarRegister = findViewById(R.id.register_progressBar_register);
    mBack = findViewById(R.id.register_button_back);

    mFirebaseAuth = FirebaseAuth.getInstance();

    mPasswordVisibility.setOnCheckedChangeListener(this::setPasswordVisibility);
    mConfirmPasswordVisibility.setOnCheckedChangeListener(this::setConfirmPasswordVisibility);
    mRegister.setOnClickListener(v -> tryToRegisterWithEmailAndPassword());
    mBack.setOnClickListener(v -> goBackToLoginScreen());
  }

  private void setPasswordVisibility(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
      mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    } else {
      mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
  }

  private void setConfirmPasswordVisibility(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
      mConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    } else {
      mConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
  }

  private void tryToRegisterWithEmailAndPassword() {
    String email = mEmail.getText().toString();
    String password = mPassword.getText().toString();
    String confirmPassword = mConfirmPassword.getText().toString();
    if (!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
      if (password.equals(confirmPassword)) {
        mProgressBarRegister.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            goToMainScreen();
          } else {
            String error = Objects.requireNonNull(task.getException()).getMessage();
            Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
          }
        });
        mProgressBarRegister.setVisibility(View.INVISIBLE);
      } else {
        Toast.makeText(Register.this, "Seems like the confirmed password is not the same...", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(Register.this, "All fields are necessary!", Toast.LENGTH_SHORT).show();
    }
  }

  private void goToMainScreen() {
    startActivity(new Intent(Register.this, MainActivity.class));
    finish();
  }

  private void goBackToLoginScreen() {
    startActivity(new Intent(Register.this, Login.class));
    finish();
  }
}
