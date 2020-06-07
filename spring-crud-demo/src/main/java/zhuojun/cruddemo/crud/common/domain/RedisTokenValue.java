package zhuojun.cruddemo.crud.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: zhuojun
 * @description: token在redis中的存储结构
 * @date: 2020/06/07 13:57
 * @modified:
 */
@Data
@Accessors(chain = true)
public class RedisTokenValue implements Serializable {
    private static final long serialVersionUID = 3670914673875462841L;
    String token;
    String secret;
}
