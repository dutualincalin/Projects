package common;

import iofactory.IOFactory;

/**
 * Clasa utilitara pentru retinerea constantelor.
 * @author Dutu Alin Calin
 */
public final class Constants {
    private Constants() {
    }
    public static final double DIVIDING_NUMBER = 10;
    public static final double PENALTY_NUMBER = 1.2;
    public static final double PROFIT_NUMBER = 0.2;
    public static final IOFactory.IOType INPUT = IOFactory.IOType.Input;
    public static final IOFactory.IOType OUTPUT = IOFactory.IOType.Output;
    public static final String GREEN = "GREEN";
    public static final String PRICE = "PRICE";
    public static final String QUANTITY = "QUANTITY";
}
