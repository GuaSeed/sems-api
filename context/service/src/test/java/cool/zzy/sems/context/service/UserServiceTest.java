package cool.zzy.sems.context.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.*;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/1 17:48
 * @since 1.0
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:service-test.xml")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void signIn() {
        Assertions.assertNotNull(userService.signIn("1428658308@qq.com", "111111"));
    }

    @Test
    void register() {
    }
}