package cool.zzy.sems.core;

import cool.zzy.sems.context.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/30 22:42
 * @since 1.0
 */
public class CoreApplication {
    private static final Logger logger = LoggerFactory.getLogger(CoreApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("server-spring.xml");
        UserService userService = applicationContext.getBean(UserService.class);
        logger.info("{}", userService.getUserById(1));
        userService.signIn(null, null);
    }
}
