package Roles;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static Roles.Function.*;

public enum Role {
    ADMIN(Function.values()),
    MAINTAINER(START, STOP, RESTART, STATUS),
    POWER_USER(PRINT, QUEUE, RESTART, TOP_QUEUE),
    USER(PRINT, QUEUE);

    private final Set<Function> allowed;
    Role(Function... functions) {
        allowed = Arrays.stream(functions).collect(Collectors.toSet());
    }

    public boolean isAllowed(Function function) {
        return allowed.contains(function);
    }

    public static Role fromString(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}