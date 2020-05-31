package zhuojun.cruddemo.crud.auth.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhuojun
 * @date 2020-05-28 22:06
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("auth_token")
public class AuthToken extends Model<AuthToken> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * token
     */
    private String token;

    /**
     * Expire time
     */
    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    /**
     * Owner
     */
    @TableField(value = "user_id")
    private Long userId;

    @Override
    protected Serializable pkVal(){
        return this.id;
    }

}
