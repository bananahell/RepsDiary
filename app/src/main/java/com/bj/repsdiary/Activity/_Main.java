package com.bj.repsdiary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bj.repsdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class _Main extends AppCompatActivity {

  private Button mLogout;
  private FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mLogout = findViewById(R.id.main_button_logout);
    mLogout.setOnClickListener(v -> {
      mFirebaseAuth.signOut();
      Intent intent = new Intent(_Main.this, Login.class);
      startActivity(intent);
      finish();
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser == null) {
      Intent intent = new Intent(_Main.this, Login.class);
      startActivity(intent);
      finish();
    }
  }
}
