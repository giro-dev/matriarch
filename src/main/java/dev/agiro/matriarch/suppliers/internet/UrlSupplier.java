package dev.agiro.matriarch.suppliers.internet;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random URLs.
 */
public class UrlSupplier extends RandomSupplier<String> {

    private static final String[] PROTOCOLS = {"https://", "http://"};

    private static final String[] DOMAINS = {
            "example.com", "test.com", "demo.com", "sample.org", "website.net",
            "company.com", "service.io", "platform.co", "app.dev", "site.com"
    };

    private static final String[] PATHS = {
            "/home", "/about", "/products", "/services", "/contact", "/api/v1",
            "/dashboard", "/profile", "/settings", "/docs", "/blog", "/help"
    };

    @Override
    public String get() {
        String protocol = randomElement(PROTOCOLS);
        String domain = randomElement(DOMAINS);

        // 50% chance of adding a path
        if (RANDOM.nextBoolean()) {
            return protocol + domain + randomElement(PATHS);
        }

        return protocol + domain;
    }
}

