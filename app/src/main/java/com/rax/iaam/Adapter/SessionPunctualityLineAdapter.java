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

public class SessionPunctualityLineAdapter extends RecyclerView.Adapter<SessionPunctualityLineAdapter.SessionPunctualityLineAdapterViewHolder> {
    List<ArrayList<String>> ondataTime;

    Context context;


    public SessionPunctualityLineAdapter(List<ArrayList<String>> ondataTime) {
        this.ondataTime = ondataTime;

    }

    @NonNull
    @Override
    public SessionPunctualityLineAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_session_line, parent, false);
        context = parent.getContext();
        return new SessionPunctualityLineAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionPunctualityLineAdapterViewHolder holder, int position) {


        final TextView[] myTextViews = new TextView[ondataTime.get(position).size()];

        for (int i = 0; i < ondataTime.get(position).size(); i++) {
            final TextView rowTextView = new TextView(context);
            rowTextView.setText(ondataTime.get(position).get(i));
            rowTextView.setTextSize(15);
            rowTextView.setGravity(Gravity.CENTER);
            rowTextView.setBackgroundColor(context.getResources().getColor(R.color.color7));
            LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
            Params1.setMargins(3, 1, 0, 0);
            rowTextView.setLayoutParams(Params1);
            holder.linearLayout.addView(rowTextView);
            myTextViews[i] = rowTextView;

        }

    }

    @Override
    public int getItemCount() {
        return ondataTime.size();
    }

    public class SessionPunctualityLineAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView percentage;
        private LinearLayout linearLayout;

        public SessionPunctualityLineAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
        }
    }
}
