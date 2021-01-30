package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;
import cool.zzy.sems.core.db.mapper.UserMapper;
import cool.zzy.sems.rpc.common.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;

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
    private ValueOperations valueOperations;

    @Override
    public User getUserById(Integer id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectUserById(id);
    }

    @Override
    public User signIn(String ukEmail, String password) {
        logger.info("{}", valueOperations.get("test"));
        return null;
    }

    @Override
    public User register(User user) {
        return null;
    }
}
