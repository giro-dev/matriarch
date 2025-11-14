package dev.agiro.matriarch.suppliers.financial;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random ISO 4217 currency codes.
 */
public class CurrencyCodeSupplier extends RandomSupplier<String> {

    private static final String[] CURRENCY_CODES = {
            "USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD", "NZD",
            "CNY", "INR", "BRL", "MXN", "ZAR", "KRW", "SGD", "HKD",
            "NOK", "SEK", "DKK", "PLN", "THB", "IDR", "MYR", "PHP",
            "TRY", "RUB", "AED", "SAR", "ILS", "EGP", "ARS", "CLP",
            "COP", "PEN", "CZK", "HUF", "RON", "VND"
    };

    @Override
    public String get() {
        return randomElement(CURRENCY_CODES);
    }
}

