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
public class SessionPunctualityAdapter extends RecyclerView.Adapter<SessionPunctualityAdapter.SessionPunctualityViewHolder> {

    ArrayList<String> xvalue;
    Context context;

    public SessionPunctualityAdapter(ArrayList<String> xvalue) {
        this.xvalue = xvalue;
    }

    @NonNull
    @Override
    public SessionPunctualityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_session_punctuality_heatmap, parent, false);
        context = parent.getContext();
        return new SessionPunctualityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionPunctualityViewHolder holder, int position) {
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
        Params1.setMargins(3, 1, 0, 0);
       holder.percentage.setLayoutParams(Params1);
        holder.percentage.setText(xvalue.get(position));
//        if (position == 0) {
//            holder.percentage.setText("Date");
//        } else {
//            holder.percentage.setText(xvalue.get(position));
//        }

    }

    @Override
    public int getItemCount() {
        return xvalue.size() ;
    }

    public class SessionPunctualityViewHolder extends RecyclerView.ViewHolder {
        private TextView percentage,date;
        private LinearLayout linearLayout;

        public SessionPunctualityViewHolder(@NonNull View itemView) {
            super(itemView);
            percentage = itemView.findViewById(R.id.txt_session_data);
            linearLayout = itemView.findViewById(R.id.LinearLayout2);
            }
    }
}
