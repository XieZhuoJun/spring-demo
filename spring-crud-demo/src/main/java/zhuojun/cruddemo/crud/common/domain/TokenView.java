package zhuojun.cruddemo.crud.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * @author zhuojun
 * @date 2020-05-28 22:06
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TokenView {

    private static final long serialVersionUID = 1L;


    /**
     * UUID
     */
    private String uuid;

    /**
     * Owner
     */
    private Long userId;

    /**
     * Role id
     */
    private Integer roleId;

    /**
     * platform id
     */
    private Integer platformId;

    /**
     * Expire time
     */
    private LocalDateTime expireTime;
}
