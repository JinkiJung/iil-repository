package net.getko.iilrepository.models.domain;

public enum IilState {
    NOTACTIVATED("notactivated"),
    ACTIVE("active"),
    FOCUSED("focused"),
    PENDING("pending"),
    FINISHED("finished"),
    ACHIEVED("achieved");

    private final String value;

    private IilState(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static IilState fromValue(String v) {
        IilState[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            IilState c = var1[var3];
            if (c.value.equals(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }
}
