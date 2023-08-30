package net.getko.iilrepository.utils;

import net.getko.iilrepository.models.domain.Action;

public class ActionValidator {
    public static boolean validate(Action action) {
        if (action.getId() == null || action.getId().toString().isEmpty()) {
            return false;
        }
        if (action.getName() == null) {
            return false;
        }
        return true;
    }
}
