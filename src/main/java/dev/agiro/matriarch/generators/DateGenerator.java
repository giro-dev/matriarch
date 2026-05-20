package dev.agiro.matriarch.generators;


import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

import java.util.Date;

public class DateGenerator extends AbstractGenerator<Date> {

    public DateGenerator() {
        super(Date.class);
    }


    @Override
    public Date generate(Definition supplierInput) {
        return new Date(System.currentTimeMillis() - Math.abs(GenerationContext.getInstance().getRandom().nextLong()));
    }
}
