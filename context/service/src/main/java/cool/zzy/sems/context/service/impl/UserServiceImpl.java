package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;
import cool.zzy.sems.core.db.config.GlobalConfig;
import cool.zzy.sems.core.db.mapper.UserMapper;
import cool.zzy.sems.core.util.HashUtils;
import cool.zzy.sems.rpc.common.annotation.RpcService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/29 21:01
 * @since 1.0
 */
@RpcService(value = UserService.class, version = 1)
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public User signIn(String email, String password) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return null;
        }
        // 先在redis存在是否存在
        User user = redisTemplate.opsForValue().get(globalConfig.getRedisSignInUserPrefix() + email);
        if (user != null) {
            // redis有缓存，比对md5
            // 因为如果redis有缓存，说明密码是被hash过的
            if (user.getPasswordHash().equals(password)) {
                // 更新缓存时间
                redisTemplate.expire(globalConfig.getRedisSignInUserPrefix() + email,
                        globalConfig.getRedisSignInUserExpire(), TimeUnit.SECONDS);
                return user;
            }
            // 验证失败直接清除缓存
            redisTemplate.delete(globalConfig.getRedisSignInUserPrefix() + email);
            return null;
        }
        // 从数据库中查询是否存在该账户
        user = userMapper.selectUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (HashUtils.verify(password, user.getPasswordHash(), globalConfig.getPasswordHashCount())) {
            // 去除密码中的随机盐
            user.setPasswordHash(HashUtils.removeSalt(user.getPasswordHash()));
            // 缓存到redis
            redisTemplate.opsForValue().set(globalConfig.getRedisSignInUserPrefix() + user.getUkEmail(), user,
                    globalConfig.getRedisSignInUserExpire(), TimeUnit.SECONDS);
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) throws Exception {
        if (user == null || StringUtils.isBlank(user.getUkEmail())
                || StringUtils.isBlank(user.getPasswordHash())) {
            throw new Exception("Email or password can not null.");
        }
        // 将明文的密码md5后存储
        user.setPasswordHash(HashUtils.generate(user.getPasswordHash(),
                HashUtils.randomSalt(), globalConfig.getPasswordHashCount()));
        user.setCreated(System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(1));
        if (StringUtils.isBlank(user.getNickname())) {
            user.setNickname(user.getUkEmail());
        }
        if (user.getGender() == null) {
            user.setGender(true);
        }
        try {
            if (userMapper.insertUser(user) > 0) {
                // 已经注册好用户，去除密码中的随机盐
                user.setPasswordHash(HashUtils.removeSalt(user.getPasswordHash()));
                // 缓存到redis
                redisTemplate.opsForValue().set(globalConfig.getRedisSignInUserPrefix() + user.getUkEmail(), user,
                        globalConfig.getRedisSignInUserExpire(), TimeUnit.SECONDS);
                return user;
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            throw new Exception("Email " + user.getUkEmail() + " already exists.");
        }
        return null;
    }

    @Override
    public User updateUser(User user) {
        if (user == null || StringUtils.isBlank(user.getUkEmail())) {
            return null;
        }
        // 删除redis中的缓存
        redisTemplate.delete(globalConfig.getRedisSignInUserPrefix() + user.getUkEmail());
        user.setModified(System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(1));
        try {
            if (userMapper.updateUser(user) > 0) {
                user = userMapper.selectUserByEmail(user.getUkEmail());
                if (user == null) {
                    return null;
                }
                // 去除密码中的随机盐
                user.setPasswordHash(HashUtils.removeSalt(user.getPasswordHash()));
                // 缓存到redis
                redisTemplate.opsForValue().set(globalConfig.getRedisSignInUserPrefix() + user.getUkEmail(), user,
                        globalConfig.getRedisSignInUserExpire(), TimeUnit.SECONDS);
                return user;
            }
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean logout(String email) {
        return redisTemplate.delete(globalConfig.getRedisSignInUserPrefix() + email);
    }
}
