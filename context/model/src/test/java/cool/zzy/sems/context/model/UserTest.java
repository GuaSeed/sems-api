package cool.zzy.sems.context.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/30 21:25
 * @since 1.0
 */
class UserTest {

    @Test
    void getId() {
        User user = new User();
        user.setId(1);
        Assertions.assertEquals(1, user.getId());
    }
}