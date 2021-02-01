package cool.zzy.sems.core.db.mapper;

import cool.zzy.sems.context.model.Config;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 15:51
 * @since 1.0
 */
public interface ConfigMapper {
    /**
     * 获取一个激活的Config
     *
     * @return {@link Config}
     */
    @Results({
            @Result(property = "passwordHashCount", column = "password_hash_count"),
            @Result(property = "redisSignInUserPrefix", column = "redis_sign_in_user_prefix"),
            @Result(property = "redisSignInUserExpire", column = "redis_sign_in_user_expire"),
    })
    @Select("select id,\n" +
            "       extract(epoch from created)  as created,\n" +
            "       extract(epoch from modified) as modified,\n" +
            "       password_hash_count,\n" +
            "       redis_sign_in_user_prefix,\n" +
            "       redis_sign_in_user_expire\n" +
            "from t_config\n" +
            "where is_deleted = false\n" +
            "order by created desc\n" +
            "limit 1")
    Config getActiveConfig();
}
