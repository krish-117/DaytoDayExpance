package com.example.daytodayexpance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpance.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomSheet extends BottomSheetDialogFragment {

    FragmentBottomSheetBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int c_d =0;
    public BottomSheet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false);
        String Email = getArguments().getString("Email","").replace('.',',');
        String Date = getArguments().getString("Date","");
        String Month = getArguments().getString("Month","");
        String Year = getArguments().getString("Year","");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        binding.tvcredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvcredit.setTextColor(Color.parseColor("#CDE7E4"));
                binding.tvDebit.setTextColor(Color.parseColor("#9D9D9F"));
                c_d=0;
            }
        });

        binding.tvDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvDebit.setTextColor(Color.parseColor("#CDE7E4"));
                binding.tvcredit.setTextColor(Color.parseColor("#9D9D9F"));
                c_d = 1;
            }
        });

        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = binding.etText.getText().toString().trim();
                String amountText = binding.edAmmount.getText().toString().trim();


                if (text.isEmpty()) {
                    binding.etText.setError("Enter text");
                    return;
                } else if (amountText.isEmpty()) {
                    binding.edAmmount.setError("Enter Amount");
                    return;
                } else {
                    try {
                        int amount = Integer.parseInt(amountText);
                    } catch (NumberFormatException e) {
                        binding.edAmmount.setError("Enter Valid Amount");
                        return;
                    }
                }

                if (c_d == 0
                ) {
                    databaseReference.child("Data").child(Email).child(Year).child(Month).child(Date).child("Cedit").child(text).setValue(amountText);
                }
                else {
                    databaseReference.child("Data").child(Email).child(Year).child(Month).child(Date).child("Debit").child(text).setValue(amountText);
                }
                dismiss();

            }
        });

        return binding.getRoot();
    }
}