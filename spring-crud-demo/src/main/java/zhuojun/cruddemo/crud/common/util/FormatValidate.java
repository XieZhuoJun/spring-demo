package zhuojun.cruddemo.crud.common.util;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 18:45
 * @modified:
 */
public class FormatValidate {

    /**
     * @description 密码格式验证
     * @param password password to be verified
     * @return Boolean
     */
    public static Boolean verifyPassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }

        else{
        return true;}
    }

    /**
     * @TODO Test
     * @param uuid
     * @return
     */
    public static Boolean verifyUUID(String uuid){
        //Only Numbers, alphabetic characters and '-' are allowed
        //No restrictions for length
        for(Char c : uuid.toCharArray()){
            if(!((c >= 0 && c <= 9) || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))){
                return false;
            }
        }        
        return true;
    }
}
