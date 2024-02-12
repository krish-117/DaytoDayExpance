package com.example.daytodayexpance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daytodayexpance.databinding.ListdailyBinding;
import com.example.daytodayexpance.databinding.ListmonthlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MonthlyRecyclerAdapter extends RecyclerView.Adapter<MonthlyRecyclerAdapter.ViewHolder> {

    private List<String> key_debit;
    private String email, currentMonth, currentYear;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Context context;

    public MonthlyRecyclerAdapter(List<String> key_debit, String email, String currentMonth, String currentYear) {
        this.currentYear = currentYear;
        this.email = email;
        this.currentMonth = currentMonth;
        this.key_debit = key_debit;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public MonthlyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmonthly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyRecyclerAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return key_debit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListmonthlyBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListmonthlyBinding.bind(itemView);
        }

        public void setData(int position) {
            String date = key_debit.get(position);
            binding.date.setText(date + " " + currentMonth + "," + currentYear);

            setupDebitTransactions(date);
        }


        private void setupDebitTransactions(String date) {

            databaseReference.child("Data").child(email.replace('.', ','))
                    .child(currentYear).child(currentMonth).addChildEventListener(new ChildEventListener() {

                        int total = 0;

                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            List<String> list3 = new ArrayList<>();
                            List<String> list4 = new ArrayList<>();
                            total = 0;

                            databaseReference.child("Data").child(email.replace('.', ','))
                                    .child(currentYear).child(currentMonth).child(date).child("Debit")
                                    .addChildEventListener(new ChildEventListener() {
                                        int balc = 0;
                                        int bald = 0;
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            list3.add(snapshot.getKey());
                                            list4.add(snapshot.getValue().toString());
                                            bald += Integer.parseInt(snapshot.getValue().toString());
                                            total -= bald;
                                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list3);
                                            binding.lv3.setAdapter(adapter3);
                                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list4);
                                            binding.lv4.setAdapter(adapter4);
                                            setListViewHeight(binding.lv3);
                                            setListViewHeight(binding.lv4);
                                            binding.tvTotal.setText(String.valueOf(total/key_debit.size()));
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

                            List<String> list1 = new ArrayList<>();
                            List<String> list2 = new ArrayList<>();


                            databaseReference.child("Data").child(email.replace('.', ','))
                                    .child(currentYear).child(currentMonth).child(date).child("Cedit")
                                    .addChildEventListener(new ChildEventListener() {
                                        int balc = 0;
                                        int bald = 0;

                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            list1.add(snapshot.getKey());
                                            list2.add(snapshot.getValue().toString());
                                            balc += Integer.parseInt(snapshot.getValue().toString());
                                            total += balc;
                                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list1);
                                            binding.lv1.setAdapter(adapter1);
                                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list2);
                                            binding.lv2.setAdapter(adapter2);
                                            adapter1.notifyDataSetChanged();
                                            adapter2.notifyDataSetChanged();
                                            setListViewHeight(binding.lv1);
                                            setListViewHeight(binding.lv2);
                                            binding.tvTotal.setText(String.valueOf(total/key_debit.size()));
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


        }
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
