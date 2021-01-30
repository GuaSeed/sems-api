package cool.zzy.sems.core.db.mapper;

import cool.zzy.sems.context.model.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/29 20:13
 * @since 1.0
 */
public interface UserMapper {
    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return {@link User}
     */
    @Results({
            @Result(property = "ukEmail", column = "uk_email"),
            @Result(property = "passwordHash", column = "password_hash"),
    })
    @Select("select id, created, modified, uk_email, password_hash, nickname, gender, host(ip) as ip from t_user where id = #{id} and is_deleted = false")
    User selectUserById(int id);
}
