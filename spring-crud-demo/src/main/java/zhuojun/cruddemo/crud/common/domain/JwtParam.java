package zhuojun.cruddemo.crud.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/03 16:12
 * @modified:
 */
@Accessors(chain = true)
@Data
public class JwtParam {
    private Long id;
    private String secret;
    private Integer roleId;
    private Long expireTime;
    private Integer platform;
}