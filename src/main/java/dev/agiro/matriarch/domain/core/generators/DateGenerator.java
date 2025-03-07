package dev.agiro.matriarch.domain.core.generators;


import dev.agiro.matriarch.domain.model.Definition;

import java.security.SecureRandom;
import java.util.Date;

public class DateGenerator extends AbstractGenerator<Date> {

    public DateGenerator() {
        super(Date.class);
    }


    @Override
    public Date generate(Definition supplierInput) {
        return new Date(System.currentTimeMillis() - new SecureRandom().nextLong());
    }
}
