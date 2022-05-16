package net.getko.iilrepository.models.domain;

public enum IilStatus {
    NOTINITIATED("notinitiated"),
    ACTIVE("active"),
    FOCUSED("focused"),
    PENDING("pending"),
    SETTLED("settled");

    private final String value;

    private IilStatus(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static IilStatus fromValue(String v) {
        IilStatus[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            IilStatus c = var1[var3];
            if (c.value.equals(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }
}
