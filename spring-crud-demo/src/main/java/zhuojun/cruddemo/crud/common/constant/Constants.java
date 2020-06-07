package zhuojun.cruddemo.crud.common.constant;

/**
 * @author: zhuojun
 * @description: Common Constants
 * @date: 2020/05/29 12:37
 * @modified:
 */
public class Constants {
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer";
    public static final Integer SUCCESS = 1;
    public static final Integer FAILURE = 0;
    public static final Integer ERROR = -1;

    /**
     * 过期时间1小时
     */
    public static final Long EXPIRE_TIME = 1000 * 60 * 60L;

    public static final String ROLE_CLAIM_KEY = "role";
    public static final String PLATFORM_CLAIM_KEY = "platform";
    public static final String UUID_CLAIM_KEY = "uuid";
}
