package dev.agiro.matriarch.generators;



import dev.agiro.matriarch.domain.core.GenerationContext;
import dev.agiro.matriarch.domain.model.Definition;

public class DoubleGenerator extends AbstractGenerator<Double> {

    public DoubleGenerator() {
        super(Double.class);
    }

    @Override
    public Double generate(Definition supplierInput) {
        return GenerationContext.getInstance().getRandom().nextDouble();
    }
}
