package dev.agiro.matriarch.suppliers.financial;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random bank account numbers.
 * Default format is 10-12 digits.
 */
public class AccountNumberSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        int length = randomInt(10, 13); // 10 to 12 digits
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < length; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }

        return accountNumber.toString();
    }
}

