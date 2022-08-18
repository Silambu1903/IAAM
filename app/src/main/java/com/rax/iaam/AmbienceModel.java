package com.rax.iaam;

import androidx.databinding.ObservableField;

public class AmbienceModel {
    public ObservableField<String> temperature = new ObservableField<String>();
    public ObservableField<String> light = new ObservableField<String>();
    public ObservableField<String> sound = new ObservableField<String>();
    public ObservableField<String> humidity = new ObservableField<String>();
    public ObservableField<String> airQuality = new ObservableField<String>();
    public ObservableField<String> pressure = new ObservableField<String>();
    public ObservableField<String> motion = new ObservableField<String>();
}
