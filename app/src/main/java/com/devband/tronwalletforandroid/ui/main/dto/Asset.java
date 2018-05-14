package com.devband.tronwalletforandroid.ui.main.dto;

import java.text.DecimalFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    private static DecimalFormat df = new DecimalFormat("#,##0.00000000");

    private String name;

    private double balance;

    @Override
    public String toString() {
        return name + " (" + df.format(balance) + ")";
    }
}
