package zhuojun.cruddemo.crud.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/05 21:58
 * @modified:
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserWithTokenStatus extends User{
    private Set<TokenView> availableTokens;

}
