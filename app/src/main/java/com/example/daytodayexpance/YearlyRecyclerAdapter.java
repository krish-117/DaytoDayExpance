package com.example.daytodayexpance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daytodayexpance.databinding.ListyearlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class YearlyRecyclerAdapter extends RecyclerView.Adapter<YearlyRecyclerAdapter.ViewHolder>{
    String email;
    String currentyear;
    List<String> month = new ArrayList<>();


    public YearlyRecyclerAdapter(String userEmail, String currentYear) {
        this.email = userEmail;
        this.currentyear = currentYear;
    }

    @NonNull
    @Override
    public YearlyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listyearly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearlyRecyclerAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return month.size();
    }


    public void setMonth(List<String> month) {
        this.month = month;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListyearlyBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListyearlyBinding.bind(itemView);
        }

        public void setData(int position) {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Data")
                    .child(email.replace(".", ","))
                    .child(currentyear);


            databaseReference1.child(month.get(position)).addChildEventListener(new ChildEventListener() {
                int balc = 0;
                int bald = 0;
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String currentdate = snapshot.getKey();

                    databaseReference1.child(month.get(position)).child(currentdate).child("Cedit").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String val = snapshot.getValue().toString();
                            int valu = Integer.parseInt(val);
                            balc = balc + valu;
                            binding.tvCredit.setText(String.valueOf(balc));
                            binding.tvTotal.setText(String.valueOf(balc-bald));
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

                    databaseReference1.child(month.get(position)).child(currentdate).child("Debit").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String val = snapshot.getValue().toString();
                            int valu = Integer.parseInt(val);
                            bald = bald + valu;
                            binding.tvDebit.setText(String.valueOf(bald));
                            binding.tvTotal.setText(String.valueOf(balc-bald));
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

            binding.tvmonth.setText(month.get(position));

        }
    }

}
