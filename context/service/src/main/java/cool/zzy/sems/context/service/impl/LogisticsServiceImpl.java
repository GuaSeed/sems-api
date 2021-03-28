package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.model.Logistics;
import cool.zzy.sems.context.service.LogisticsService;
import cool.zzy.sems.core.db.mapper.LogisticsMapper;
import cool.zzy.sems.rpc.common.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 14:00
 * @since 1.0
 */
@RpcService(value = LogisticsService.class, version = 1)
public class LogisticsServiceImpl implements LogisticsService {
    @Autowired
    private LogisticsMapper logisticsMapper;

    @Override
    public List<Logistics> getListByUserId(Integer uid) {
        if (uid == null) {
            return Collections.emptyList();
        }
        return logisticsMapper.selectListByUserId(uid);
    }

    @Override
    public boolean save(Logistics logistics) {
        if (logistics == null) {
            return false;
        }
        logistics.setCreated(System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(1));
        return logisticsMapper.insert(logistics) > 0;
    }
}
