package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.Delivery;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 14:41
 * @since 1.0
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:service-test.xml")
@Log
class DeliveryServiceTest {
    @Autowired
    private DeliveryService deliveryService;

    @Test
    void getListByUid() {
        List<Delivery> deliveryList = deliveryService.getListByUid(1);
        System.out.println(deliveryList);
    }

    @Test
    void save() {
        Delivery delivery = new Delivery();
        delivery.setUserId(1);
        delivery.setPostId("11111111111111");
        delivery.setDeliveryCompanyId(317);
        delivery.setLocationName("xxxxxxx");
        delivery.setPhone("xxxxxxxx");
        System.out.println(deliveryService.save(delivery));
    }
}