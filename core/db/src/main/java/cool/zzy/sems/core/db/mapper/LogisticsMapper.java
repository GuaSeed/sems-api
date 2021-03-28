package cool.zzy.sems.core.db.mapper;

import cool.zzy.sems.context.model.Logistics;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 14:00
 * @since 1.0
 */
public interface LogisticsMapper {
    /**
     * 根据uid查询物流信息
     *
     * @param uid uid
     * @return list
     */
    @Results({
            @Result(property = "delete", column = "is_deleted"),
            @Result(property = "userId", column = "fk_user_id"),
            @Result(property = "deliveryId", column = "fk_delivery_id"),
            @Result(property = "currentLocation", column = "current_location"),
    })
    @Select("select a.id,\n" +
            "       extract(epoch from a.created)  as created,\n" +
            "       extract(epoch from a.modified) as modified,\n" +
            "       a.is_deleted,\n" +
            "       a.fk_delivery_id,\n" +
            "       a.current_location,\n" +
            "       a.fk_user_id\n" +
            "from t_logistics as a,\n" +
            "     t_delivery as b\n" +
            "where b.fk_user_id = #{uid}\n" +
            "  and b.id = a.fk_delivery_id\n" +
            "  and b.is_complete = false\n" +
            "  and b.is_deleted = false\n" +
            "  and a.is_deleted = false\n" +
            "order by created desc;")
    List<Logistics> selectListByUserId(int uid);


    /**
     * 增加物流信息
     *
     * @param logistics 物流信息
     * @return >=1 success
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert(value = "insert into t_logistics(created, fk_delivery_id, current_location, fk_user_id)\n" +
            "VALUES (to_timestamp(#{created}), #{deliveryId}, #{currentLocation}, #{userId});")
    int insert(Logistics logistics);

    /**
     * 删除物流信息
     */


}
