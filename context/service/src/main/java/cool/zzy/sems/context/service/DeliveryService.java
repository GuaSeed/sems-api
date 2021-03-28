package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.Delivery;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 13:42
 * @since 1.0
 */
public interface DeliveryService {
    /**
     * 根据uid获取物流信息
     *
     * @param uid uid
     * @return 物流信息
     */
    List<Delivery> getListByUid(Integer uid);

    /**
     * 添加一条物流信息
     *
     * @param delivery 物流信息
     * @return true add success
     */
    boolean save(Delivery delivery);

}
