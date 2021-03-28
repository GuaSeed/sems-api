package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.Logistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 14:00
 * @since 1.0
 */
public interface LogisticsService {
    /**
     * 根据uid查询物流信息
     *
     * @param uid uid
     * @return list
     */
    List<Logistics> getListByUserId(Integer uid);


    /**
     * 增加物流信息
     *
     * @param logistics 物流信息
     * @return >=1 success
     */
    boolean save(Logistics logistics);
}
