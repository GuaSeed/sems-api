package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;
import cool.zzy.sems.core.db.config.GlobalConfig;
import cool.zzy.sems.core.db.mapper.UserMapper;
import cool.zzy.sems.core.util.HashUtils;
import cool.zzy.sems.rpc.common.annotation.RpcService;
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
            redisTemplate.opsForValue().set(globalConfig.getRedisSignInUserPrefix() + user.getUkEmail(), user,
                    globalConfig.getRedisSignInUserExpire(), TimeUnit.SECONDS);
            return user;
        }
        return null;
    }

    @Override
    public User register(User user) {
        return null;
    }
}
