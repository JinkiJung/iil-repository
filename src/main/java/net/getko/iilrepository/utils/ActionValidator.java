package net.getko.iilrepository.utils;

import net.getko.iilrepository.models.domain.Act;

public class ActionValidator {
    public static boolean validate(Act act) {
        if (act.getId() == null || act.getId().toString().isEmpty()) {
            return false;
        }
        if (act.getName() == null) {
            return false;
        }
        return true;
    }
}
