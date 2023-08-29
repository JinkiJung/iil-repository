package net.getko.iilrepository.utils;

import net.getko.iilrepository.models.domain.Condition;

public class ConditionValidator {
    public static boolean validate(Condition condition) {
        if (condition.getId() == null || condition.getId().toString().isEmpty()) {
            return false;
        }
        if (condition.getName() == null) {
            return false;
        }
        if (condition.getShortName() == null) {
            return false;
        }
        if (condition.getType() == null) {
            return false;
        }
        return true;
    }
}
