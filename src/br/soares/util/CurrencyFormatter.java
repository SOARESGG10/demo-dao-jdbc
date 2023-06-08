package br.soares.util;

import java.text.DecimalFormat;

public class CurrencyFormatter {

    public static String format(Number value) {
        return DecimalFormat.getCurrencyInstance().format(value);
    }

}
