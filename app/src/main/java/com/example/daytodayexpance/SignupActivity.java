package com.example.daytodayexpance;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.example.daytodayexpance.databinding.ActivitySignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    int checked = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        binding.etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etFirstName.setBackground(getDrawable(R.drawable.borderfocus));
                binding.etLastName.setBackground(getDrawable(R.drawable.background));
                binding.llPassword.setBackground(getDrawable(R.drawable.background));
                binding.etPhoneNumber.setBackground(getDrawable(R.drawable.background));
                binding.etEmail.setBackground(getDrawable(R.drawable.background));
            }
        });

        binding.etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etFirstName.setBackground(getDrawable(R.drawable.background));
                binding.etLastName.setBackground(getDrawable(R.drawable.borderfocus));
                binding.llPassword.setBackground(getDrawable(R.drawable.background));
                binding.etPhoneNumber.setBackground(getDrawable(R.drawable.background));
                binding.etEmail.setBackground(getDrawable(R.drawable.background));
            }
        });

        binding.etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etFirstName.setBackground(getDrawable(R.drawable.background));
                binding.etLastName.setBackground(getDrawable(R.drawable.background));
                binding.llPassword.setBackground(getDrawable(R.drawable.background));
                binding.etPhoneNumber.setBackground(getDrawable(R.drawable.borderfocus));
                binding.etEmail.setBackground(getDrawable(R.drawable.background));
            }
        });

        binding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etFirstName.setBackground(getDrawable(R.drawable.background));
                binding.etLastName.setBackground(getDrawable(R.drawable.background));
                binding.llPassword.setBackground(getDrawable(R.drawable.background));
                binding.etPhoneNumber.setBackground(getDrawable(R.drawable.background));
                binding.etEmail.setBackground(getDrawable(R.drawable.borderfocus));
            }
        });

        binding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.etFirstName.setBackground(getDrawable(R.drawable.background));
                binding.etLastName.setBackground(getDrawable(R.drawable.background));
                binding.llPassword.setBackground(getDrawable(R.drawable.borderfocus));
                binding.etPhoneNumber.setBackground(getDrawable(R.drawable.background));
                binding.etEmail.setBackground(getDrawable(R.drawable.background));
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

        binding.tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });


        binding.btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
                Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

                if (binding.etFirstName.getText().toString().equals(""))
                    binding.etFirstName.setError("Enter First Name");
                else if (binding.etLastName.getText().toString().equals("")) {
                    binding.etLastName.setError("Enter First Name");
                } else if (binding.etPhoneNumber.getText().toString().length() != 10) {
                    binding.etPhoneNumber.setError("Enter Valid Phone Number");
                } else if (!binding.etEmail.getText().toString().matches(emailPattern)
                        || binding.etEmail.getText().toString().equals("")) {
                    binding.etEmail.setError("Enter Valid Email");
                } else if (binding.etPassword.getText().toString().equals("")) {
                    binding.etPassword.setError("Enter Password");
                } else if (!pattern.matcher(binding.etPassword.getText().toString()).matches()) {
                    Toast.makeText(SignupActivity.this, "PASSWORD MUST CONTAIN: number,special character,capital ", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase();
                }
            }
        });
    }

    private void addDatatoFirebase() {
        String email = binding.etEmail.getText().toString().replace('.', ',');
        String firstName = binding.etFirstName.getText().toString();
        String lastName = binding.etLastName.getText().toString();
        String mobile = binding.etPhoneNumber.getText().toString();
        String password = binding.etPassword.getText().toString();

        databaseReference.child("Data").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(SignupActivity.this, "Email is registered", Toast.LENGTH_SHORT).show();
                    binding.etEmail.setError("Try Different Email id");
                    return;
                }
                else
                {

                    // Set values in the "Data" node
                    databaseReference.child("Data").child(email).child("FirstName").setValue(firstName);
                    databaseReference.child("Data").child(email).child("LastName").setValue(lastName);
                    databaseReference.child("Data").child(email).child("Mobile").setValue(mobile);
                    databaseReference.child("Data").child(email).child("password").setValue(password);
                    databaseReference.child("Data").child(email).child("Email").setValue(email);

                    // Set value in the "FindUser" node
                    databaseReference.child("FindUser").child(mobile).setValue(email);

                    // Clear input fields
                    binding.etFirstName.setText("");
                    binding.etLastName.setText("");
                    binding.etEmail.setText("");
                    binding.etPhoneNumber.setText("");
                    binding.etPassword.setText("");

                    SharedPreferences sharedPref =  getApplicationContext().getSharedPreferences("EmailShare",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Email", email.replace(',','.'));
                    editor.apply();

                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                    intent.putExtra("Email",email.replace(',','.'));
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}