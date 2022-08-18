package com.rax.iaam;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class ClockTimeModel {
    public ObservableField<String> poweredOnPercent = new ObservableField<String>();
    public ObservableField<String> operatedPercent = new ObservableField<String>();
    public ObservableField<String> idlePercent = new ObservableField<String>();
}
