package util;

public enum IdPrefix {
    VEH("VEH"),
    OWN("OWN"),
    CUS("CUS"),
    CAN("CAN"),
    PEN("PEN"),
    INV("INV"),
    WAL("WAL"),
    TRX("TRX");

    private final String value;

    IdPrefix(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
