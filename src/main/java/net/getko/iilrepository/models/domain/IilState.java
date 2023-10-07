package net.getko.iilrepository.models.domain;

public enum IilState {
    NOT_ACTIVATED("not_activated"),
    ACTIVATED("activated"),
    PENDING("pending"),
    FINISHED("finished"),
    ABORTED("aborted");
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
