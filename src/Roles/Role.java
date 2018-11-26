package Roles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public enum Role {
    ADMIN,
    MAINTAINER,
    POWER_USER,
    USER;

    private final Set<Method> allowed;
    Role() {
        try {
            allowed =
                Files.lines(Paths.get("Role-policy.txt"))
                        .filter(str -> str.startsWith(name()))
                        .flatMap(l -> {
                            List<String> words =
                                    Arrays.stream(l.split(","))
                                            .collect(Collectors.toCollection(LinkedList::new));
                            words.remove(0);
                            return words.stream();
                        })
                        .map(String::trim)
                        .map(Method::valueOf)
                        .collect(Collectors.toCollection(HashSet::new));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Fix the Role-policy.txt document and rerun the program");
        }
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