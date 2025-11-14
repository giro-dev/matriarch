package dev.agiro.matriarch.suppliers.company;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random job titles.
 */
public class JobTitleSupplier extends RandomSupplier<String> {

    private static final String[] JOB_TITLES = {
            "Software Engineer", "Senior Software Engineer", "Lead Software Engineer",
            "Software Architect", "DevOps Engineer", "Data Engineer", "Data Scientist",
            "Product Manager", "Project Manager", "Program Manager", "Engineering Manager",
            "QA Engineer", "Test Engineer", "Frontend Developer", "Backend Developer",
            "Full Stack Developer", "Mobile Developer", "UI/UX Designer", "Product Designer",
            "Business Analyst", "Systems Administrator", "Database Administrator",
            "Network Engineer", "Security Engineer", "Cloud Engineer", "Site Reliability Engineer",
            "Technical Lead", "Team Lead", "CTO", "VP of Engineering", "Director of Engineering",
            "Marketing Manager", "Sales Manager", "Account Manager", "HR Manager",
            "Financial Analyst", "Operations Manager", "Customer Success Manager",
            "Support Engineer", "Solutions Architect", "Consultant", "Principal Engineer"
    };

    @Override
    public String get() {
        return randomElement(JOB_TITLES);
    }
}

