package com.rax.iaam.Others;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.rax.iaam.R;

import java.util.Calendar;

public class YearDialogbox extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;
    private static final String TAG = "YearDialogbox";
    private Context context;

    public void setListener(DatePickerDialog.OnDateSetListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Are you sure?");
        Calendar cal = Calendar.getInstance();
        View dialog = inflater.inflate(R.layout.year_dialog_box, null);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        final Button ok = (Button) dialog.findViewById(R.id.dialog_ok);
        final Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(2000);
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);
        builder.setView(dialog);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked");
                listener.onDateSet(null, yearPicker.getValue(),0, 0);
                YearDialogbox.this.getDialog().dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearDialogbox.this.getDialog().dismiss();
            }
        });

      /*  builder.setView(dialog)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, yearPicker.getValue(),0, 0);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        YearDialogbox.this.getDialog().cancel();
                    }
                });*/
        return builder.create();
    }

}
