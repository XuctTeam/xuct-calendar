/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ParamException
 * Author:   Derek Xu
 * Date:     2021/12/6 15:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.exception;

import cn.com.xuct.calendar.common.core.res.IResultCode;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
public class SvrException extends BaseException{


    public SvrException(Integer code) {
        super(code);
    }

    public SvrException(String message, Integer code) {
        super(message, code);
    }

    public SvrException(IResultCode resultCode) {
        super(resultCode);
    }
}