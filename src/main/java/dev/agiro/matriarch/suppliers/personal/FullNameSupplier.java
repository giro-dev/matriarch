package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random full names (first name + last name).
 */
public class FullNameSupplier extends RandomSupplier<String> {

    private final FirstNameSupplier firstNameSupplier = new FirstNameSupplier();
    private final LastNameSupplier lastNameSupplier = new LastNameSupplier();

    @Override
    public String get() {
        return firstNameSupplier.get() + " " + lastNameSupplier.get();
    }
}

