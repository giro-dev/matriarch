package dev.agiro.matriarch.suppliers.datetime;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

import java.util.TimeZone;

/**
 * Supplier that generates random timezone IDs.
 */
public class TimeZoneSupplier extends RandomSupplier<String> {

    private static final String[] TIMEZONES = {
            "UTC",
            "America/New_York",
            "America/Chicago",
            "America/Denver",
            "America/Los_Angeles",
            "America/Toronto",
            "America/Mexico_City",
            "America/Sao_Paulo",
            "America/Buenos_Aires",
            "Europe/London",
            "Europe/Paris",
            "Europe/Berlin",
            "Europe/Madrid",
            "Europe/Rome",
            "Europe/Amsterdam",
            "Europe/Brussels",
            "Europe/Stockholm",
            "Europe/Warsaw",
            "Europe/Moscow",
            "Europe/Istanbul",
            "Asia/Dubai",
            "Asia/Karachi",
            "Asia/Kolkata",
            "Asia/Bangkok",
            "Asia/Singapore",
            "Asia/Hong_Kong",
            "Asia/Shanghai",
            "Asia/Tokyo",
            "Asia/Seoul",
            "Asia/Jakarta",
            "Australia/Sydney",
            "Australia/Melbourne",
            "Australia/Brisbane",
            "Pacific/Auckland",
            "Pacific/Fiji",
            "Africa/Cairo",
            "Africa/Johannesburg",
            "Africa/Lagos"
    };

    @Override
    public String get() {
        return randomElement(TIMEZONES);
    }
}

