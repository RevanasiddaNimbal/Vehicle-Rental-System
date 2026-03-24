package util;

public enum IdPrefix {
    VEH("VEH"),
    OWN("OWN"),
    CUS("CUS"),
    RENT("RNT"),
    CAN("CAN"),
    PEN("PEN"),
    INV("INV");

    private final String value;

    IdPrefix(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
