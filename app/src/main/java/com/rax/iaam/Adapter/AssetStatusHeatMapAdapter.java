package com.rax.iaam.Adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssetStatusHeatMapAdapter extends RecyclerView.Adapter<AssetStatusHeatMapAdapter.AssetStatusHeatMapViewHolder> {
    // private String[] data;
    private LinkedHashMap<String,LinkedHashMap<String, String>> values;
    Map<String, LinkedHashMap<LinkedHashMap<String, String>, String>> totalValue;

    public AssetStatusHeatMapAdapter(LinkedHashMap<String,LinkedHashMap<String, String>> availability) {
        this.values = availability;
    }

    public AssetStatusHeatMapAdapter() {

    }

    @NonNull
    @Override
    public AssetStatusHeatMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset_status_heatmap, parent, false);
        return new AssetStatusHeatMapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetStatusHeatMapViewHolder holder, int position) {
        /*  holder.percentage.setText(data[position]); new String[]{monthWiseList.getJSONObject(j).getString("availability")}
          int dummy = Integer.parseInt(data[position]);
        if (dummy < 10) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (dummy < 20) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (dummy < 30) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (dummy < 40) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (dummy < 50) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (dummy < 60) {
            holder.percentage.setBackgroundColor(Color.parseColor("#DB4437"));
        } else {
            holder.percentage.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }*/

        List<String> lineNames = new ArrayList<String>(values.keySet());

        List<String> lastone = new ArrayList<>();

        List<String> toolValue = new ArrayList<>();
        for (int i = 0; i < lineNames.size(); i++) {
            String name_type = lineNames.get(i);
            String[] bits = name_type.split("-");
            String lastOnes = bits[bits.length-1];
            String headername = name_type.substring(0, name_type.lastIndexOf(" -"));
            toolValue.add(name_type);
            lastone.add(lastOnes);
        }
        List<LinkedHashMap<String, String>> lineValues = new ArrayList<LinkedHashMap<String, String>>(values.values());
        holder.lineName.setText(lastone.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.lineName.setTooltipText(toolValue.get(position));
        }
        try {
            if (lineValues.get(position).get("Jan") != null) {
                chooseColor(lineValues.get(position).get("Jan"), holder.jan);
            } else {
                holder.jan.setText("N/A");
            }
        } catch (Exception e) {
            holder.jan.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Feb") != null) {
                chooseColor(lineValues.get(position).get("Feb"), holder.feb);
            } else {
                holder.feb.setText("N/A");
            }
        } catch (Exception e) {
            holder.feb.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Mar") != null) {
                chooseColor(lineValues.get(position).get("Mar"), holder.mar);
            } else {
                holder.mar.setText("N/A");
            }
        } catch (Exception e) {
            holder.mar.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Apr") != null) {
                chooseColor(lineValues.get(position).get("Apr"), holder.apr);
            } else {
                holder.apr.setText("N/A");
            }
        } catch (Exception e) {
            holder.apr.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("May") != null) {
                chooseColor(lineValues.get(position).get("May"), holder.may);
            } else {
                holder.may.setText("N/A");
            }
        } catch (Exception e) {
            holder.may.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Jun") != null) {
                chooseColor(lineValues.get(position).get("Jun"), holder.jun);
            } else {
                holder.jun.setText("N/A");
            }
        } catch (Exception e) {
            holder.jun.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Jul") != null) {
                chooseColor(lineValues.get(position).get("Jul"), holder.jul);
            } else {
                holder.jul.setText("N/A");
            }
        } catch (Exception e) {
            holder.jul.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Aug") != null) {
                chooseColor(lineValues.get(position).get("Aug"), holder.aug);
            } else {
                holder.aug.setText("N/A");
            }
        } catch (Exception e) {
            holder.aug.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Sep") != null) {
                chooseColor(lineValues.get(position).get("Sep"), holder.sep);
            } else {
                holder.sep.setText("N/A");
            }
        } catch (Exception e) {
            holder.sep.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Oct") != null) {
                chooseColor(lineValues.get(position).get("Oct"), holder.oct);
            } else {
                holder.oct.setText("N/A");
            }
        } catch (Exception e) {
            holder.oct.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Nov") != null) {
                chooseColor(lineValues.get(position).get("Nov"), holder.nov);
            } else {
                holder.nov.setText("N/A");
            }
        } catch (Exception e) {
            holder.nov.setText("N/A");
        }

        try {
            if (lineValues.get(position).get("Dec") != null) {
                chooseColor(lineValues.get(position).get("Dec"), holder.dec);
            } else {
                holder.dec.setText("N/A");
            }
        } catch (Exception e) {
            holder.dec.setText("N/A");
        }

    }

    private void chooseColor(String value, TextView txtView) {
        float mValue = Float.parseFloat(value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtView.setTooltipText(value);
        }

        txtView.setText(value);
        if (mValue < 10) {
            txtView.setBackgroundColor(Color.parseColor("#ef9a9a"));
        } else if (mValue < 20) {
            txtView.setBackgroundColor(Color.parseColor("#e57373"));
        } else if (mValue < 30) {
            txtView.setBackgroundColor(Color.parseColor("#ef5350"));
        } else if (mValue < 40) {
            txtView.setBackgroundColor(Color.parseColor("#f44336"));
        } else if (mValue < 50) {
            txtView.setBackgroundColor(Color.parseColor("#e53935"));
        } else if (mValue < 60) {
            txtView.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (mValue < 70) {
            txtView.setBackgroundColor(Color.parseColor("#d32f2f"));
        } else if (mValue < 80) {
            txtView.setBackgroundColor(Color.parseColor("#DB4437"));
        } else if (mValue < 90) {
            txtView.setBackgroundColor(Color.parseColor("#b71c1c"));
        } else if (mValue < 100) {
            txtView.setBackgroundColor(Color.parseColor("#d50000"));
        } else {
            txtView.setBackgroundColor(Color.parseColor("#eeff41"));
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class AssetStatusHeatMapViewHolder extends RecyclerView.ViewHolder {
        private TextView lineName, jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec;

        public AssetStatusHeatMapViewHolder(@NonNull View itemView) {
            super(itemView);
            lineName = itemView.findViewById(R.id.txt_AssetStatus_heatMap0);
            jan = itemView.findViewById(R.id.txt_AssetStatus_heatMap1);
            feb = itemView.findViewById(R.id.txt_AssetStatus_heatMap2);
            mar = itemView.findViewById(R.id.txt_AssetStatus_heatMap3);
            apr = itemView.findViewById(R.id.txt_AssetStatus_heatMap4);
            may = itemView.findViewById(R.id.txt_AssetStatus_heatMap5);
            jun = itemView.findViewById(R.id.txt_AssetStatus_heatMap6);
            jul = itemView.findViewById(R.id.txt_AssetStatus_heatMap7);
            aug = itemView.findViewById(R.id.txt_AssetStatus_heatMap8);
            sep = itemView.findViewById(R.id.txt_AssetStatus_heatMap9);
            oct = itemView.findViewById(R.id.txt_AssetStatus_heatMap10);
            nov = itemView.findViewById(R.id.txt_AssetStatus_heatMap11);
            dec = itemView.findViewById(R.id.txt_AssetStatus_heatMap12);

        }
    }
}
