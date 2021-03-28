package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/10 19:48
 * @since 1.0
 */
@Data
public class Delivery implements Serializable {

    private static final long serialVersionUID = -8734688086581430553L;
    private Integer id;
    private Long created;
    private Long modified;
    private Boolean delete;
    private Integer userId;
    private Integer postId;
    private Integer deliveryId;
    private String remark;
    private Boolean complete;
    private String locationName;
}
