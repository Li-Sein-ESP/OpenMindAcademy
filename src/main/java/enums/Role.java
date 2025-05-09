package enums;

public enum Role {
    TEACHER, STUDENT, ADMIN;

    @Override
    public String toString() {
        return switch (this) {
            case TEACHER -> "Teacher";
            case STUDENT -> "Student";
            case ADMIN -> "Admin";
        };
    }
}
