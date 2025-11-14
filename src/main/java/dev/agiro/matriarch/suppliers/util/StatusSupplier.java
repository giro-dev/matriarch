package dev.agiro.matriarch.suppliers.util;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random status values.
 */
public class StatusSupplier extends RandomSupplier<String> {

    private static final String[] STATUSES = {
            "ACTIVE",
            "INACTIVE",
            "PENDING",
            "SUSPENDED",
            "ARCHIVED",
            "DELETED",
            "DRAFT",
            "PUBLISHED",
            "COMPLETED",
            "IN_PROGRESS",
            "CANCELLED",
            "EXPIRED"
    };


    @Override
    public String get() {
        return randomElement(STATUSES);
    }
}

