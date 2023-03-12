package org.example;

import lombok.ToString;

import java.util.EnumMap;
import java.util.EnumSet;

public class Quantity {
    MeasurementType type;
    MeasurementUnit unit;
    double value;

    public enum MeasurementType {
        WEIGHT,
        AMOUNT,
        VOLUME,
    }

    private static final EnumSet<MeasurementUnit> weightUnits = EnumSet.of(MeasurementUnit.KILO, MeasurementUnit.GRAM);
    private static final EnumSet<MeasurementUnit> amountUnits = EnumSet.of(MeasurementUnit.PIECE);
    private static final EnumSet<MeasurementUnit> volumeUnits = EnumSet.of(MeasurementUnit.LITER, MeasurementUnit.MILLILITER);

    private static final EnumMap<MeasurementType, EnumSet<MeasurementUnit>> typeToUnitSet = new EnumMap<>(MeasurementType.class);
    static {
        typeToUnitSet.put(MeasurementType.WEIGHT, weightUnits);
        typeToUnitSet.put(MeasurementType.AMOUNT, amountUnits);
        typeToUnitSet.put(MeasurementType.VOLUME, volumeUnits);
    }

    private static final EnumMap<MeasurementUnit, Integer> conversionValues = new EnumMap<>(MeasurementUnit.class);
    static {
        conversionValues.put(MeasurementUnit.KILO, 1000);
        conversionValues.put(MeasurementUnit.GRAM, 1);

        conversionValues.put(MeasurementUnit.PIECE, 1);

        conversionValues.put(MeasurementUnit.LITER, 1000);
        conversionValues.put(MeasurementUnit.MILLILITER, 1);
    }

    @ToString
    public enum MeasurementUnit {
        // WEIGHT
        KILO,
        GRAM,

        // AMOUNT
        PIECE,

        // VOLUME
        LITER,
        MILLILITER
    }

    public void convertTo(MeasurementUnit unitToConvert) {
        if (typeToUnitSet.get(type).contains(unitToConvert)) {
            value = value * conversionValues.get(unitToConvert) / conversionValues.get(unit);
            unit = unitToConvert;
        } else {
            throw new DifferentMeasurementTypesException(unit, unitToConvert);
        }
    }

    public static class DifferentMeasurementTypesException extends RuntimeException {
        public DifferentMeasurementTypesException(MeasurementUnit from, MeasurementUnit to) {
            super("Tried to convert to unit of another measurement type: from " + from + " to " + to);
        }
    }

}
