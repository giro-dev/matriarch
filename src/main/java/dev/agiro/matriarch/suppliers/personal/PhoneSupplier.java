package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random US phone numbers in format (XXX) XXX-XXXX.
 * For international formats, consider creating specific suppliers.
 */
public class PhoneSupplier extends RandomSupplier<String> {

    @Override
    public String get() {
        int areaCode = randomInt(200, 999);
        int prefix = randomInt(200, 999);
        int lineNumber = randomInt(1000, 9999);

        return String.format("(%03d) %03d-%04d", areaCode, prefix, lineNumber);
    }
}

