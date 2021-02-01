package cool.zzy.sems.core.db.mapper;

import cool.zzy.sems.context.model.User;
import org.apache.ibatis.annotations.Insert;
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
    @Select("select id,\n" +
            "       extract(epoch from created)  as created,\n" +
            "       extract(epoch from modified) as modified,\n" +
            "       uk_email,\n" +
            "       password_hash,\n" +
            "       nickname,\n" +
            "       gender,\n" +
            "       host(ip)                     as ip\n" +
            "from t_user\n" +
            "where id = #{id}\n" +
            "  and is_deleted = false\n" +
            "limit 1")
    User selectUserById(int id);


    /**
     * 根据email获取获取
     *
     * @param email 邮箱
     * @return {@link User}
     */
    @Results({
            @Result(property = "ukEmail", column = "uk_email"),
            @Result(property = "passwordHash", column = "password_hash"),
    })
    @Select("select id,\n" +
            "       extract(epoch from created)  as created,\n" +
            "       extract(epoch from modified) as modified,\n" +
            "       uk_email,\n" +
            "       password_hash,\n" +
            "       nickname,\n" +
            "       gender,\n" +
            "       host(ip)                     as ip\n" +
            "from t_user\n" +
            "where uk_email = #{email}\n" +
            "  and is_deleted = false\n" +
            "limit 1")
    User selectUserByEmail(String email);

    /**
     * 插入一条记录到用户表
     *
     * @param user 用户信息
     * @return 插入成功记录条数
     * @throws Exception 插入用户重复抛出异常
     */
    @Insert("insert into t_user(created, uk_email, password_hash, nickname, gender, ip)\n" +
            "values (to_timestamp(#{created}), #{ukEmail}, #{passwordHash}, #{nickname}, #{gender}, #{ip}::inet)")
    int insertUser(User user) throws Exception;
}
