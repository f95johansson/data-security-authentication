package Roles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RoleCheck {

    public static boolean isAllowed(String role, Method method) {
        try {
            return Files.lines(Paths.get("Role-policy.txt"))
                    .filter(str -> str.startsWith(role))
                    .anyMatch(l -> {
                        List<Method> words =
                                Arrays.stream(l.split(","))
                                        .skip(1)
                                        .map(String::trim)
                                        .map(Method::valueOf)
                                        .collect(Collectors.toCollection(LinkedList::new));
                        return words.contains(method);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}