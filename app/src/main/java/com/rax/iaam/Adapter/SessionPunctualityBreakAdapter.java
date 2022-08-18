package com.rax.iaam.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;
//edited on 22/12/2020 by silambu
public class SessionPunctualityBreakAdapter extends RecyclerView.Adapter<SessionPunctualityBreakAdapter.SessionPunctualityBreakAdapterViewHolder> {
    List<ArrayList<String>> ondataTimeBreak;
    Context context;


    public SessionPunctualityBreakAdapter(List<ArrayList<String>> ondataTimeBreak) {
        this.ondataTimeBreak = ondataTimeBreak;
    }

    @NonNull
    @Override
    public SessionPunctualityBreakAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_break_value, parent, false);
        context = parent.getContext();
        return new SessionPunctualityBreakAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionPunctualityBreakAdapterViewHolder holder, int position) {

        final TextView[] myTextViews = new TextView[ondataTimeBreak.get(position).size()];

        for (int i = 0; i < ondataTimeBreak.get(position).size(); i++) {
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText(ondataTimeBreak.get(position).get(i));
            rowTextView.setTextSize(15);
            rowTextView.setGravity(Gravity.CENTER);
            rowTextView.setBackgroundColor(context.getResources().getColor(R.color.color7));
            float pixels = 60 * context.getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
            Params1.setMargins(3, 1, 0, 0);
            rowTextView.setLayoutParams(Params1);
            // add the textview to the linearlayout
            holder.linearLayout.addView(rowTextView);
            // save a reference to the textview for later
            myTextViews[i] = rowTextView;

        }

    }

    @Override
    public int getItemCount() {
        return ondataTimeBreak.size();
    }


    public class SessionPunctualityBreakAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView value;
        LinearLayout linearLayout;
        public SessionPunctualityBreakAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.LinearLayout2);
         //   value = itemView.findViewById(R.id.txt_break_value);
        }
    }
}
