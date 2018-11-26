
import Roles.Method;
import Roles.Role;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static Roles.Role.*;
import static Roles.Method.*;
import static org.assertj.core.api.Assertions.*;

public class RoleTest {

    @Test
    public void user_has_print_and_queue() {
        assertOnlyTheseAllowed(USER, PRINT, QUEUE);
    }

    @Test
    public void admin_has_full_access() {
        assertOnlyTheseAllowed(ADMIN, Method.values());
    }

    @Test
    public void maintainer() {
        assertOnlyTheseAllowed(MAINTAINER, START, STOP, STATUS, RESTART);
    }

    @Test
    public void power_user() {
        assertOnlyTheseAllowed(POWER_USER, RESTART, QUEUE, TOP_QUEUE, PRINT);
    }

    private void assertOnlyTheseAllowed(Role role, Method... methods) {
        Set<Method> set = Arrays.stream(methods).collect(Collectors.toSet());

        for (Method method : Method.values()) {
            if (set.contains(method)) {
                assertThat(role.isAllowed(method)).isTrue();
            } else {
                assertThat(role.isAllowed(method)).isFalse();
            }
        }
    }
}
