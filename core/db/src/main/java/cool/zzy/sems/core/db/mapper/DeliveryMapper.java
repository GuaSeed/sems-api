package cool.zzy.sems.core.db.mapper;

import cool.zzy.sems.context.model.Delivery;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 13:50
 * @since 1.0
 */
public interface DeliveryMapper {
    /**
     * 根据uid查询到快递信息
     *
     * @param uid uid
     * @return list
     */
    @Results({
            @Result(property = "delete", column = "is_deleted"),
            @Result(property = "userId", column = "fk_user_id"),
            @Result(property = "postId", column = "post_id"),
            @Result(property = "deliveryCompanyId", column = "fk_delivery_company_id"),
            @Result(property = "complete", column = "is_complete"),
            @Result(property = "locationName", column = "location_name"),
    })
    @Select("select id,\n" +
            "       extract(epoch from created)  as created,\n" +
            "       extract(epoch from modified) as modified,\n" +
            "       fk_user_id,\n" +
            "       post_id,\n" +
            "       fk_delivery_company_id,\n" +
            "       remark,\n" +
            "       is_complete,\n" +
            "       location_name,\n" +
            "       phone\n" +
            "from t_delivery\n" +
            "where fk_user_id = #{uid}\n" +
            "  and is_deleted = false")
    List<Delivery> selectListByUserId(int uid);

    /**
     * 增加快递信息
     *
     * @param delivery 快递信息
     * @return >=1 success
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert(value = "insert into t_delivery(created, fk_user_id, post_id, fk_delivery_company_id, remark, location_name, phone)\n" +
            "VALUES (to_timestamp(#{created}), #{userId}, #{postId}, #{deliveryCompanyId}, #{remark}, #{locationName}, #{phone})")
    int insert(Delivery delivery);
}
