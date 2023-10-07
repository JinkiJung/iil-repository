package net.getko.iilrepository.models.domain;

// Condition type includes iil input, time, location, iil variable, iil state, event.
public enum ConditionType {
    TIME("time"),
    LOCATION("location"),
    IIL_INPUT("iil_input"),
    IIL_VARIABLE("iil_variable"),
    IIL_STATE("iil_state"),
    EVENT("event");

    private final String value;

    private ConditionType(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static ConditionType fromValue(String v) {
        ConditionType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ConditionType c = var1[var3];
            if (c.value.equals(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }
}
