/**
 * Copyright (C), 2021-2022, 楚恬商行
 * FileName: ComponentAttachment
 * Author:   Derek Xu
 * Date:     2022/5/27 18:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2022/5/27
 * @since 1.0.0
 */
@Data
@TableName("cms_component_attachment")
public class ComponentAttachment extends SuperEntity<ComponentAttachment> {

    /**
     * 事件ID
     */
    private Long componentId;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 路径
     */
    private String path;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 访问地址
     */
    private String domain;

    /**
     * uuid
     */
    private String uuid;
}