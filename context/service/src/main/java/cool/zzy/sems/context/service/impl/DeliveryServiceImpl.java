package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.service.DeliveryService;
import cool.zzy.sems.core.db.mapper.DeliveryMapper;
import cool.zzy.sems.rpc.common.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 13:43
 * @since 1.0
 */
@RpcService(value = DeliveryService.class, version = 1)
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public List<Delivery> getListByUid(Integer uid) {
        if (uid == null) {
            return Collections.emptyList();
        }
        return deliveryMapper.selectListByUserId(uid);
    }

    @Override
    public boolean save(Delivery delivery) {
        if (delivery == null) {
            return false;
        }
        delivery.setCreated(System.currentTimeMillis() / TimeUnit.SECONDS.toMillis(1));
        return deliveryMapper.insert(delivery) > 0;
    }
}
