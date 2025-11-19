package dev.agiro.matriarch.suppliers.company;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random department names.
 */
public class DepartmentSupplier extends RandomSupplier<String> {

    private static final String[] DEPARTMENTS = {
            "Engineering", "Software Development", "Product", "Design", "Marketing",
            "Sales", "Customer Support", "Human Resources", "Finance", "Operations",
            "Legal", "Quality Assurance", "Research and Development", "IT", "Security",
            "Data Analytics", "Business Intelligence", "Administration", "Procurement",
            "Supply Chain", "Manufacturing", "Logistics", "Customer Success", "Training",
            "Compliance", "Risk Management", "Corporate Strategy", "Public Relations"
    };

    @Override
    public String get() {
        return randomElement(DEPARTMENTS);
    }
}

