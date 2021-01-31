package cool.zzy.sems.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 16:30
 * @since 1.0
 */
class HashUtilsTest {

    @Test
    void passwordHash() {
        System.out.println(HashUtils.passwordHash("123456", "123456", 2));
    }
}