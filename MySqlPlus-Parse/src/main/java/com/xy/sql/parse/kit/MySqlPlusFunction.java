package com.xy.sql.parse.kit;

import java.util.Map;

/**
 * 核心工具类
 */
public class MySqlPlusFunction {


    public static Object action(Object value, String funcName, Map<String,String> paraMap){
        if("AUTO_INCREMENT".equalsIgnoreCase(funcName.trim())){
            return Snowflake.getInstance().nextId();
        }
        return value;
    }
}
