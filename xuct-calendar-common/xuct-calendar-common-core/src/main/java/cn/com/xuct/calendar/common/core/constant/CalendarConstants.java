/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ColorConstants
 * Author:   Derek Xu
 * Date:     2021/12/2 15:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.core.constant;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/2
 * @since 1.0.0
 */
public interface CalendarConstants {


     class Color {
        public static final String BLUE = "3399ff";
    }


    class AlarmType {
        public static final Integer APP = 0;

        public static final Integer EMAIL = 1;

        public static final Integer WXMAPP = 2;

        public static final Integer SMS = 3;
    }


    class AlarmTime {
         public static final int FIFTEEN  = 15;

         public static final int THIRTY = 30;

         public static final int SIXTY = 60;

    }

     class ShareType {
        public static final Integer NO_SHARE = 0;

        public static final Integer SHARE = 1;
    }

}