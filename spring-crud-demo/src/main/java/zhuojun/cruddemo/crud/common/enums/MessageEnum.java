package zhuojun.cruddemo.crud.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuojun
 * @date 2020-05-28 20:50
 **/
public enum MessageEnum {

    //系统返回信息时使用的Enums
    SUCCESS("00001", "操作成功"),
    ERROR("00002", "操作失败"),
    LOGIN_SUCCESS("00003", "登录成功"),
    LOGIN_ERROR("00004", "登录失败，用户名或密码错误"),
    NO_AUTH("00005", "无访问权限"),
    INVALID_TOKEN("00006", "token无效"),
    TOKEN_EXPIRED("00007", "token已过期"),
    ACCOUNT_EXIST("00008", "账户已存在"),
    IVALID_PARAM("00009", "参数错误"),
    ;

    private final static Map<String, MessageEnum> MESSAGE_ENUM_HASH_MAP = new HashMap<>();

    static {
        for (MessageEnum errorEnum : MessageEnum.values()) {
            MESSAGE_ENUM_HASH_MAP.put(errorEnum.code, errorEnum);
        }
    }

    private final String code;
    private final String msg;

    MessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(String code) {
        return MESSAGE_ENUM_HASH_MAP.get(code).msg;
    }

    public static MessageEnum getErrorEnumMap(String code) {
        return MESSAGE_ENUM_HASH_MAP.get(code);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
