package com.example.daytodayexpance;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.daytodayexpance.databinding.FragmentMonthlyBinding;
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

public class MonthlyFragment extends Fragment {

    private FragmentMonthlyBinding binding;
    private String userEmail;
    private Calendar calendar;
    private DateFormat monthYearFormatter;
    private String currentMonth, currentYear;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> debitKeys = new ArrayList<>();

    public MonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly, container, false);

        userEmail = getActivity().getIntent().getExtras().getString("Email");

        monthYearFormatter = new SimpleDateFormat("LLLL/yyyy");
        calendar = Calendar.getInstance();
        String formattedDate = monthYearFormatter.format(calendar.getTime());

        StringTokenizer monthYearTokenizer = new StringTokenizer(formattedDate, "/");
        currentMonth = monthYearTokenizer.nextToken();
        currentYear = monthYearTokenizer.nextToken();

        binding.tvmonth.setText(currentMonth);
        binding.tvYear.setText(currentYear);

        setData();
        init();

        return binding.getRoot();
    }

    private void init() {
        binding.ivlastmonth.setOnClickListener(view -> {
            calendar.add(Calendar.MONTH, -1);
            updateMonthYear();
        });

        binding.ivmonthAfter.setOnClickListener(view -> {
            calendar.add(Calendar.MONTH, 1);
            updateMonthYear();
        });
    }

    private void updateMonthYear() {
        String updatedMonthYear = monthYearFormatter.format(calendar.getTime());
        StringTokenizer updatedTokenizer = new StringTokenizer(updatedMonthYear, "/");
        currentMonth = updatedTokenizer.nextToken();
        currentYear = updatedTokenizer.nextToken();

        binding.tvmonth.setText(currentMonth);
        binding.tvYear.setText(currentYear);

        setData();
    }

    private void setData() {
        debitKeys.clear();
        MonthlyRecyclerAdapter monthlyRecyclerAdapter = new MonthlyRecyclerAdapter(debitKeys, userEmail, currentMonth, currentYear);
        binding.rv.setAdapter(monthlyRecyclerAdapter);

        databaseReference.child("Data").child(userEmail.replace('.', ','))
                .child(currentYear).child(currentMonth)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        debitKeys.add(snapshot.getKey());
                        MonthlyRecyclerAdapter updatedAdapter = new MonthlyRecyclerAdapter(debitKeys, userEmail, currentMonth, currentYear);
                        binding.rv.setAdapter(updatedAdapter);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // Handle child changed
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        // Handle child removed
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // Handle child moved
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
    }
}