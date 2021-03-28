package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/10 19:51
 * @since 1.0
 */
@Data
public class Logistics implements Serializable {

    private static final long serialVersionUID = -8641068465077144228L;
    private Integer id;
    private Long created;
    private Long modified;
    private Boolean delete;
    private Integer deliveryId;
    private Integer userId;
    private String currentLocation;
}
