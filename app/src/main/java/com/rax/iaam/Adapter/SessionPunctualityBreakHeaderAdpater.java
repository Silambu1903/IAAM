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

public class SessionPunctualityBreakHeaderAdpater  extends RecyclerView.Adapter<SessionPunctualityBreakHeaderAdpater.SessionPunctualityBreakHeaderHolder> {
    ArrayList<String> xvaluebreak;
    Context context;


    public SessionPunctualityBreakHeaderAdpater(ArrayList<String> xvaluebreak) {
        this.xvaluebreak = xvaluebreak;
    }

    @NonNull
    @Override
    public SessionPunctualityBreakHeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_break_header, parent, false);
        return new SessionPunctualityBreakHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionPunctualityBreakHeaderHolder holder, int position) {
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
        Params1.setMargins(3, 1, 0, 0);
        holder.date.setLayoutParams(Params1);
        holder.date.setText(xvaluebreak.get(position));



    }

    @Override
    public int getItemCount() {
        return xvaluebreak.size() ;
    }


    public class SessionPunctualityBreakHeaderHolder extends RecyclerView.ViewHolder {
        private TextView date;
        LinearLayout linearLayout;
        public SessionPunctualityBreakHeaderHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.LinearLayout2);

             date = itemView.findViewById(R.id.txt_break_data);
        }
    }
}
