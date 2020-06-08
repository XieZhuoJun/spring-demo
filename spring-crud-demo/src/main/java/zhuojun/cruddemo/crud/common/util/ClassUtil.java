package zhuojun.cruddemo.crud.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/05 22:46
 * @modified:
 */
public class ClassUtil {
    public static void covertFatherToChild(Object source, Object target) {
        if ((target.getClass().getSuperclass() == source.getClass())) {
            Field[] fieldList = source.getClass().getDeclaredFields();
            for (Field field : fieldList) {
                try {
                    String fname = field.getName();
                    fname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
                    Method getMethod = source.getClass().getMethod("get" + fname, null);
                    Method setMethod = target.getClass().getMethod("set" + fname, field.getType());
                    setMethod.invoke(target, getMethod.invoke(source, null));
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

}

