package dk.tandhjulet.enums;

public enum Provider {
    UNKNOWN,
    AREASHOP,
    PRISON_CELLS;

    public static Provider get(String stringProvider) {
        switch (stringProvider.toUpperCase()) {
            case "AREASHOP":
            case "AREA_SHOP":
                return Provider.AREASHOP;
            case "CELLS":
            case "PRISONCELLS":
            case "PRISON_CELLS":
                return Provider.PRISON_CELLS;
        }
        return Provider.UNKNOWN;
    }
}
