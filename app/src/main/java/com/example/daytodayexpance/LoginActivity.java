package com.example.daytodayexpance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.daytodayexpance.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    int checked = 0;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("EmailShare", Context.MODE_PRIVATE);
        email = sharedPref.getString("Email", "");

        if (!email.isEmpty()) {
            startLogin();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Email", email.replace(',', '.'));
            startActivity(intent);
        }

        binding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etEmailorPhone.setBackground(getDrawable(R.drawable.background));
                binding.llPassword.setBackground(getDrawable(R.drawable.borderfocus));
            }
        });

        binding.etEmailorPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.llPassword.setBackground(getDrawable(R.drawable.background));
                binding.etEmailorPhone.setBackground(getDrawable(R.drawable.borderfocus));
            }
        });

        binding.ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked == 0) {
                    // show password
                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checked = 1;
                } else {
                    // hide password
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    checked = 0;
                }
            }
        });

        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        binding.btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();

                if (binding.etEmailorPhone.getText().toString().equals(""))
                    binding.etEmailorPhone.setError(" ");
                else if (binding.etPassword.getText().toString().equals("")) {
                    binding.etPassword.setError("Enter First Name");
                } else {
                    findDataFromFirebase();
                }
            }
        });
    }

    private void startLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Email", email.replace(',', '.'));
        startActivity(intent);
    }

    private void findDataFromFirebase() {

        String email = binding.etEmailorPhone.getText().toString().trim().replace('.',',');
        String password = binding.etPassword.getText().toString().trim();

        Query checkUserDatabase = databaseReference.child("Data").orderByChild("Email").equalTo(email);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String passFromDb = snapshot.child(email.replace('.',',')).child("password").getValue(String.class);
                    if (passFromDb.equals(password)) {

                        SharedPreferences sharedPref =  getApplicationContext().getSharedPreferences("EmailShare",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Email", email.replace(',','.'));
                        editor.apply();
                        startLogin();

                    } else {

                        binding.etPassword.setError("Wrong password");
                        binding.etPassword.requestFocus();
                    }
                } else {

                    binding.etEmailorPhone.setError("User not exist");
                    binding.etEmailorPhone.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}