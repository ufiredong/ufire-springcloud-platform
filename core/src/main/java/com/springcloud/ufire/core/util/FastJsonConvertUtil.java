package com.springcloud.ufire.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.springcloud.ufire.core.model.ResetUser;

public class FastJsonConvertUtil {
    public static ResetUser convertJSONToObject(String message, Object obj){
        ResetUser resetUser = JSON.parseObject(message, new TypeReference<ResetUser>() {});
        return resetUser;
    }

    public static String convertObjectToJSON(Object obj){
        String text = JSON.toJSONString(obj);
        return text;
    }
}
