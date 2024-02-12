package com.example.daytodayexpance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpance.databinding.FragmentYearlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class YearlyFragment extends Fragment {

    FragmentYearlyBinding binding;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userEmail, currentYear;
    private Calendar calendar;
    private DateFormat monthYearFormatter;

    YearlyRecyclerAdapter yearlyRecyclerAdapter;

    public YearlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yearly, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        userEmail = getActivity().getIntent().getExtras().getString("Email");

        monthYearFormatter = new SimpleDateFormat("yyyy");
        calendar = Calendar.getInstance();
        String formattedDate = monthYearFormatter.format(calendar.getTime());

        StringTokenizer monthYearTokenizer = new StringTokenizer(formattedDate, "/");
        currentYear = monthYearTokenizer.nextToken();

        binding.tvYear.setText(currentYear);

        setData();
        init();

        return binding.getRoot();
    }

    private void init() {
        binding.ivlastyear.setOnClickListener(view -> {
            calendar.add(Calendar.YEAR, -1);
            updateYear();
        });

        binding.ivyearAfter.setOnClickListener(view -> {
            calendar.add(Calendar.YEAR, 1);
            updateYear();
        });
    }

    private void updateYear() {
        String updatedMonthYear = monthYearFormatter.format(calendar.getTime());
        StringTokenizer updatedTokenizer = new StringTokenizer(updatedMonthYear, "/");
        currentYear = updatedTokenizer.nextToken();

        binding.tvYear.setText(currentYear);

        setData();
    }

    public void setData() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Data")
                .child(userEmail.replace(".", ","))
                .child(currentYear);

        List<String> month = new ArrayList<>();

        yearlyRecyclerAdapter = new YearlyRecyclerAdapter(userEmail,currentYear);
        binding.rv.setAdapter(yearlyRecyclerAdapter);

        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String currentmonth = snapshot.getKey();
                month.add(currentmonth);
                yearlyRecyclerAdapter.setMonth(month);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}