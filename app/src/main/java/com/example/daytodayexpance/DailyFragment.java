package com.example.daytodayexpance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpance.databinding.FragmentDailyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DailyFragment extends Fragment {

    FragmentDailyBinding binding;

    Calendar calendar;
    DateFormat formatter;
    String Email, CurrentDate, CurrentMonth, CurrenrYear;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Map<String, String> data_Credit = new HashMap<>();
    Map<String, String> data_Debit = new HashMap<>();
    List key_credit = new ArrayList<String>();
    List key_debit = new ArrayList<String>();
    DailyRecyclerAdapter dailyRecycler_credit  = new DailyRecyclerAdapter();
    DailyRecyclerAdapter dailyRecycler_debit  = new DailyRecyclerAdapter();

    int Income=0,Expanse=0;
    public DailyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Email = getActivity().getIntent().getExtras().getString("Email");
//        Toast.makeText(getActivity(), ""+Email, Toast.LENGTH_SHORT).show();

        formatter = new SimpleDateFormat("dd/LLLL/yyyy/EEEE");
        calendar = Calendar.getInstance();
        String str = formatter.format(calendar.getTime());

        StringTokenizer st1 = new StringTokenizer(str, "/");
        CurrentDate = st1.nextToken();
        CurrentMonth = st1.nextToken();
        CurrenrYear = st1.nextToken();
        binding.tvDate.setText(CurrentDate);
        binding.tvMonthYear.setText(CurrentMonth + "," + CurrenrYear);
        binding.tvDay.setText(st1.nextToken());


        setDatavalue();

        init();

        return binding.getRoot();
    }

    private void setDatavalue() {
        // Clear existing data
        data_Credit.clear();
        data_Debit.clear();
        key_credit.clear();
        key_debit.clear();
        Income = 0 ;
        Expanse =0;

        dailyRecycler_debit.setData(data_Debit, key_debit);
        binding.rvExpance.setAdapter(dailyRecycler_debit);
        dailyRecycler_credit.setData(data_Credit, key_credit);
        binding.rvincome.setAdapter(dailyRecycler_credit);
        binding.totalExpanse.setText(String.valueOf(Expanse));
        binding.totalIncome.setText(String.valueOf(Income));

        databaseReference.child("Data").child(Email.replace('.', ','))
                .child(CurrenrYear).child(CurrentMonth).child(CurrentDate).child("Debit")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        data_Debit.put(snapshot.getKey(), snapshot.getValue().toString());
                        key_debit.add(snapshot.getKey());
                        Expanse += Integer.parseInt(snapshot.getValue().toString());
                        binding.totalExpanse.setText(String.valueOf(Expanse));
                        dailyRecycler_debit.setData(data_Debit, key_debit);
                        binding.rvExpance.setAdapter(dailyRecycler_debit);
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

        databaseReference.child("Data").child(Email.replace('.', ','))
                .child(CurrenrYear).child(CurrentMonth).child(CurrentDate).child("Cedit")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        data_Credit.put(snapshot.getKey(), snapshot.getValue().toString());
                        key_credit.add(snapshot.getKey());
                        Income += Integer.parseInt(snapshot.getValue().toString());
                        binding.totalIncome.setText(String.valueOf(Income));
                        dailyRecycler_credit.setData(data_Credit, key_credit);
                        binding.rvincome.setAdapter(dailyRecycler_credit);
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

    public void init() {
        binding.ivlastDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String previousDateStr = formatter.format(calendar.getTime());
                StringTokenizer st1 = new StringTokenizer(previousDateStr, "/");
                CurrentDate = st1.nextToken();
                CurrentMonth = st1.nextToken();
                CurrenrYear = st1.nextToken();
                binding.tvDate.setText(CurrentDate);
                binding.tvMonthYear.setText(CurrentMonth + "," + CurrenrYear);
                binding.tvDay.setText(st1.nextToken());

                setDatavalue();

            }
        });

        binding.ivTommorow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String nextDateStr = formatter.format(calendar.getTime());
                StringTokenizer st1 = new StringTokenizer(nextDateStr, "/");
                CurrentDate = st1.nextToken();
                CurrentMonth = st1.nextToken();
                CurrenrYear = st1.nextToken();
                binding.tvDate.setText(CurrentDate);
                binding.tvMonthYear.setText(CurrentMonth + "," + CurrenrYear);
                binding.tvDay.setText(st1.nextToken());

                // Call setDatavalue after updating the date
                setDatavalue();

            }
        });

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                Bundle bundle = new Bundle();
                bundle.putString("Email", Email);
                bundle.putString("Date", CurrentDate);
                bundle.putString("Month", CurrentMonth);
                bundle.putString("Year", CurrenrYear);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            }
        });
    }

}