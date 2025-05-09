package enums;

public enum Sexe {
    MALE, FEMALE;

    @Override
    public String toString() {
        return switch (this) {
            case MALE -> "Male";
            case FEMALE -> "Female";
        };
    }
}