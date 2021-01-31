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
            @Result(property = "passwordSalt", column = "password_salt"),
            @Result(property = "passwordHashCount", column = "password_hash_count"),
    })
    @Select("select id,\n" +
            "       created,\n" +
            "       modified,\n" +
            "       password_salt,\n" +
            "       password_hash_count\n" +
            "from t_config\n" +
            "where is_deleted = false\n" +
            "order by created desc\n" +
            "limit 1")
    Config getActiveConfig();
}
