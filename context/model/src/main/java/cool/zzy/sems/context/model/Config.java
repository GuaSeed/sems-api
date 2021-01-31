package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 15:43
 * @since 1.0
 */
@Data
public class Config implements Serializable {
    private static final long serialVersionUID = 6885511379065530377L;
    private Long created;
    private Long modified;
    private Integer passwordHashCount;
    private Boolean delete;
}
