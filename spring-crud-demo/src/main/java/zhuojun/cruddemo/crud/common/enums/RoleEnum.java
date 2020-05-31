package zhuojun.cruddemo.crud.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuojun
 * @date 2020-05-28 23:17
 **/
public enum RoleEnum {

    /**
     * 游客
     */
    VISITOR(0,"visitor"),

    /**
     * 普通用户
     */
    USER(1,"user"),

    /**
     * 管理员
     */
    ADMIN(2,"admin"),
    ;
    private static final Map<Integer, String> ROLE_TYPE_MAP = new HashMap<>();

    static {
        for(RoleEnum roleEnum : RoleEnum.values()) {
            ROLE_TYPE_MAP.put(roleEnum.getRoleId(),roleEnum.getRoleName());
        }
    }

    private final Integer roleId;
    private final String roleName;

    RoleEnum(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public static Map<Integer, String> getRoleTypeMap() {
        return ROLE_TYPE_MAP;
    }

    public Integer getRoleId(){
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }
}
