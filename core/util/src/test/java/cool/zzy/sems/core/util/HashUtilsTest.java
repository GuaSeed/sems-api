package cool.zzy.sems.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 16:30
 * @since 1.0
 */
class HashUtilsTest {

    @Test
    void generate() {
        Assertions.assertEquals("8a7w1j4bbgfeep4mdofk8i7e6a0q7edu2abqduckfndt4qfi5mdy4ien6q8q2cfc",
                HashUtils.generate("123456",
                        "awjbgepmokieaqeuaqukntqimyinqqcc", 2));
    }

    @Test
    void randomSalt() {
        String salt = HashUtils.randomSalt();
        Assertions.assertEquals(32, salt.length());
        System.out.println(salt);
    }

    @Test
    void verify() {
        Assertions.assertTrue(HashUtils.verify("123456",
                "8a7w1j4bbgfeep4mdofk8i7e6a0q7edu2abqduckfndt4qfi5mdy4ien6q8q2cfc", 2));
    }
}