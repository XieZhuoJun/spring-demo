package zhuojun.cruddemo.crud.common.domain;

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
 * @date 2020-05-28 23:28
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "user")
public class User extends Model<User> {


    private static final long serialVersionUID = -8391370345088427310L;
    /**
     * Primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Username
     */
    private String username;

    /**
     * Role
     */
    @TableField(value = "role_id")
    private Integer roleId;

    /**
     * Create time
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * Password
     */
    private String password;

    /**
     * User status
     */
    private String status;

    /**
     * Mail
     */
    private String email;

    /**
     * Mobile
     */
    private String mobile;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
