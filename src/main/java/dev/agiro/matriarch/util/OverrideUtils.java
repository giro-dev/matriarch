package dev.agiro.matriarch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.agiro.matriarch.domain.model.Overrider;
import dev.agiro.matriarch.junit.annotations.OverrideField;
import dev.agiro.matriarch.junit.annotations.internal.NoSupplier;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OverrideUtils {

    public static Map<String, Overrider> computeOverrideDefinitions(OverrideField[] overrides,
                                                                    String jsonOverrides,
                                                                    boolean jsonWins) {
        Map<String, Overrider> map = new java.util.HashMap<>();
        Map<String, Overrider> arrayMap = flattenOverridesArray(overrides);
        Map<String, Overrider> jsonMap = OverrideUtils.flattenJsonNodes(jsonOverrides);

        if (jsonWins) {
            map.putAll(arrayMap);
            map.putAll(jsonMap);
        } else {
            map.putAll(jsonMap);
            map.putAll(arrayMap);
        }
        return map;
    }

    private static Map<String, Overrider> flattenJsonNodes(String jsonOverrides) {
        if (jsonOverrides == null || jsonOverrides.isBlank()) {
            return Map.of();
        }

        try {
            return OverrideUtils.flattenJsonNodes(new ObjectMapper().readTree(jsonOverrides));
        } catch (JsonProcessingException e) {
            throw new ParameterResolutionException("Invalid jsonOverrides: " + e.getMessage(), e);
        }
    }

    public static Map<String, Overrider> flattenOverridesArray(OverrideField[] overrides) {
        return Arrays.stream(overrides)
                .collect(Collectors.toMap(
                        OverrideField::field,
                        of -> {
                            Class<? extends Supplier<?>> supplierClass = of.supplier();
                            if (supplierClass != null && supplierClass != NoSupplier.class) {
                                try {
                                    Supplier<?> supplier = supplierClass.getDeclaredConstructor().newInstance();
                                    return Overrider.supplier(supplier);
                                } catch (Exception e) {
                                    throw new ParameterResolutionException("Failed to instantiate Supplier for field '" + of.field() + "': " + e.getMessage(), e);
                                }
                            }
                            Overrider.OverriderType type = of.isRegex()
                                    ? Overrider.OverriderType.REGEX
                                    : of.type();
                            return new Overrider(of.value(), type);
                        }
                ));
    }

    public static Map<String, Overrider> flattenJsonNodes(JsonNode node) {
        Map<String, Overrider> out = new java.util.HashMap<>();
        flattenJsonNodes(node, "", out);
        return out;
    }

    private static void flattenJsonNodes(JsonNode node, String path, Map<String, Overrider> out) {
        if (node.isValueNode()) {
            var type = node.isTextual() ? Overrider.OverriderType.STRING : Overrider.OverriderType.OBJECT;
            out.put(path, new Overrider(node.asText(), type));
            return;
        }
        if (node.isObject()) {
            node.fields().forEachRemaining(e -> {
                String p = path.isEmpty() ? e.getKey() : path + "." + e.getKey();
                flattenJsonNodes(e.getValue(), p, out);
            });
            return;
        }
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String p = path + ".[" + i + "]";
                flattenJsonNodes(node.get(i), p, out);
            }
        }
    }
}
