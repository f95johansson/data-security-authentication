package Roles;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static Roles.Method.*;

public enum Role {
    ADMIN(Method.values()),
    MAINTAINER(START, STOP, RESTART, STATUS),
    POWER_USER(PRINT, QUEUE, RESTART, TOP_QUEUE),
    USER(PRINT, QUEUE);

    private final Set<Method> allowed;
    Role(Method... methods) {
        allowed = Arrays.stream(methods).collect(Collectors.toSet());
    }

    public boolean isAllowed(Method method) {
        return allowed.contains(method);
    }

    public static Role fromString(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}