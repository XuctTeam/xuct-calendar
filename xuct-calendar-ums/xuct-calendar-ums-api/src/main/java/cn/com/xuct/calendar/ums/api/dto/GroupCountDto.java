/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: GroupCountDto
 * Author:   Derek Xu
 * Date:     2022/2/14 22:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈群组统计列表〉
 *
 * @author Derek Xu
 * @create 2022/2/14
 * @since 1.0.0
 */
@Data
public class GroupCountDto implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String images;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createMemberId;

    private String createMemberName;

    private Integer count;
}