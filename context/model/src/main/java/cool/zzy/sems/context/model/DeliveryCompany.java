package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/10 19:50
 * @since 1.0
 */
@Data
public class DeliveryCompany implements Serializable {
    private static final long serialVersionUID = 2813074815382005593L;
    private Integer id;
    private Long created;
    private Long modified;
    private Boolean delete;
    private String companyName;
}
