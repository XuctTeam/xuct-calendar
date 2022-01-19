package cn.com.xuct.calendar.common.core.vo;

/**
 * Copyright (C), 2002-2018, 北京二六三企业通信有限公司
 * package : com.net263.framework.cisclient.vo
 * FileName: CommonEnum
 * Author:   xutao
 * Date:     2018/8/16 14:57
 * Description: 枚举工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * xutao           修改时间           版本号              描述
 */

public interface CommonEnum {

    //此处对应枚举的字段,如状态枚举StatusEnum定义了code,name,desc
    //那么这里定义这个三个字段的get方法,可以获取到所有的字段
    int getCode();

    String getName();
}
