package cool.zzy.sems.core.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 16:20
 * @since 1.0
 */
public class HashUtils {
    private static final int PASSWORD_HASH_LENGTH = 32;
    private static final int SALT_LENGTH = 32;
    private static final char SALT_START_CHAR = 'a';
    private static final char SALT_STOP_CHAR = 'z';

    public static String randomSalt() {
        return randomSalt(SALT_LENGTH);
    }

    /**
     * 生成count位随机盐
     *
     * @param count 位数
     * @return salt
     */
    public static String randomSalt(int count) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append((char) (SALT_START_CHAR + random.nextInt(SALT_STOP_CHAR - SALT_START_CHAR + 1)));
        }
        return sb.toString();
    }

    /**
     * 生成hash后的值
     *
     * @param password  原始密码
     * @param salt      盐
     * @param hashCount hash次数
     * @return hash后的密码
     */
    public static String generate(String password, String salt, int hashCount) {
        if (salt.length() != SALT_LENGTH) {
            throw new IllegalArgumentException("Salt length error.");
        }
        String passwordHash = md5(password + salt, hashCount);
        // 将salt和hash后的密码按1：1比例插入生成最后的密码
        char[] hashChars = new char[PASSWORD_HASH_LENGTH + SALT_LENGTH];
        for (int i = 0; i < PASSWORD_HASH_LENGTH + SALT_LENGTH; i += 2) {
            hashChars[i] = passwordHash.charAt(i / 2);
            hashChars[i + 1] = salt.charAt(i / 2);
        }
        return new String(hashChars);
    }

    /**
     * md5密码
     *
     * @param passwordSalt 原始密码和盐
     * @param hashCount    hash次数
     * @return md5
     */
    public static String md5(String passwordSalt, int hashCount) {
        if (StringUtils.isBlank(passwordSalt)) {
            throw new NullPointerException("Password can not be null.");
        }
        String passwordHash = passwordSalt;
        for (int i = 0; i < hashCount; i++) {
            passwordHash = DigestUtils.md5Hex(passwordHash);
        }
        return passwordHash;
    }

    /**
     * 效验密码是否正确
     *
     * @param password  原始密码
     * @param md5       md5后包含盐的密码
     * @param hashCount hash次数
     * @return true 正确，false 错误
     */
    public static boolean verify(String password, String md5, int hashCount) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(md5)
                || md5.length() != PASSWORD_HASH_LENGTH + SALT_LENGTH) {
            return false;
        }
        char[] passwordHash = new char[PASSWORD_HASH_LENGTH];
        char[] salt = new char[SALT_LENGTH];
        for (int i = 0; i < PASSWORD_HASH_LENGTH + SALT_LENGTH; i += 2) {
            passwordHash[i / 2] = md5.charAt(i);
            salt[i / 2] = md5.charAt(i + 1);
        }
        return new String(passwordHash).equals(md5(password + new String(salt), hashCount));
    }

    /**
     * 去除掉md5中的随机盐
     *
     * @param md5 md5
     * @return passwordHash
     */
    public static String removeSalt(String md5) {
        if (StringUtils.isBlank(md5) || md5.length() != PASSWORD_HASH_LENGTH + SALT_LENGTH) {
            return md5;
        }
        char[] passwordHash = new char[PASSWORD_HASH_LENGTH];
        for (int i = 0; i < PASSWORD_HASH_LENGTH + SALT_LENGTH; i += 2) {
            passwordHash[i / 2] = md5.charAt(i);
        }
        return new String(passwordHash);
    }
}
