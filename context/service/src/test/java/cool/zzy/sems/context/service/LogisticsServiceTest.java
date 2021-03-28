package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.Logistics;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 15:04
 * @since 1.0
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "classpath:service-test.xml")
@Log
class LogisticsServiceTest {
    @Autowired
    private LogisticsService logisticsService;

    @Test
    void getListByUserId() {
        List<Logistics> logisticsList = logisticsService.getListByUserId(1);
        System.out.println(logisticsList);
    }

    @Test
    void save() {
        Logistics logistics = new Logistics();
        logistics.setDeliveryId(1);
        logistics.setUserId(1);
        logistics.setCurrentLocation("xxxxxxxx");
        System.out.println(logisticsService.save(logistics));
    }
}