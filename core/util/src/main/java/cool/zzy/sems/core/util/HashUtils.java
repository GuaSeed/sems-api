package cool.zzy.sems.core.util;

import cool.zzy.sems.context.model.Config;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 16:20
 * @since 1.0
 */
public class HashUtils {
    public static String passwordHash(String password, Config config) {
        if (config == null) {
            throw new NullPointerException("Global config can not be null.");
        }
        return passwordHash(password, config.getPasswordSalt(), config.getPasswordHashCount());
    }

    /**
     * hash密码
     *
     * @param password  原始密码
     * @param hashSalt  hash盐
     * @param hashCount hash次数
     * @return hash后的密码
     */
    public static String passwordHash(String password, String hashSalt, int hashCount) {
        if (StringUtils.isBlank(password)) {
            throw new NullPointerException("Password can not be null.");
        }
        String ret = password + hashSalt;
        for (int i = 0; i < hashCount; i++) {
            ret = DigestUtils.md5Hex(ret);
        }
        return ret;
    }
}
