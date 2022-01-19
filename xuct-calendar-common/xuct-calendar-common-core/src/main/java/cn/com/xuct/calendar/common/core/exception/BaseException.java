/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: BaseException
 * Author:   Derek Xu
 * Date:     2021/11/15 11:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.exception;

import cn.com.xuct.calendar.common.core.res.IResultCode;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
public class BaseException extends RuntimeException{

    @Getter
    private Integer code;

    public BaseException(Integer code) {
        this.code = code;
    }


    public BaseException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BaseException(IResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}