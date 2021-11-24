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

import com.bj.repsdiary.Model.User;
import com.bj.repsdiary.R;
import com.bj.repsdiary.Utils.FirebaseHandling;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

  private EditText mName;
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

    mName = findViewById(R.id.register_edittext_name);
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
    User user = new User();
    String name = mName.getText().toString();
    String email = mEmail.getText().toString();
    String password = mPassword.getText().toString();
    String confirmPassword = mConfirmPassword.getText().toString();
    user.setUserName(name);
    user.setEmail(email);

    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
      if (password.equals(confirmPassword)) {
        mProgressBarRegister.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            user.setId(mFirebaseAuth.getUid());
            user.saveToDB();
            goToMainScreen();
          } else {
            String error = FirebaseHandling.errorHandling(getApplicationContext(), task.getException());
            Toast.makeText(Register.this, error, Toast.LENGTH_LONG).show();
          }
        });
        mProgressBarRegister.setVisibility(View.INVISIBLE);
      } else {
        Toast.makeText(Register.this, R.string.password_not_same, Toast.LENGTH_LONG).show();
      }
    } else {
      Toast.makeText(Register.this, R.string.all_fields_necessary, Toast.LENGTH_LONG).show();
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
