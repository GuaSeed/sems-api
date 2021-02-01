package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/1 17:48
 * @since 1.0
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:service-test.xml")
class UserServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    void signIn() {
        Assertions.assertNotNull(userService.signIn("1428658308@qq.com", "111111"));
    }

    @Test
    void register() {
        User user = new User();
        user.setUkEmail("142865830@qq.com");
        user.setPasswordHash("111111");
        user.setIp("127.0.0.1");
        try {
            logger.info("{}", userService.register(user));
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
    }
}