/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: IGroupService
 * Author:   Derek Xu
 * Date:     2022/2/7 16:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service;

import cn.com.xuct.calendar.service.base.IBaseService;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.boot.mapper.GroupMapper;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/7
 * @since 1.0.0
 */
public interface IGroupService extends IBaseService<GroupMapper, Group> {

    /**
     * 查询会员下组，包括人数
     *
     * @param memberId
     * @return
     */
    List<GroupInfoDto> findGroupCountByMember(Long memberId);

    /**
     * 查询群组下总数
     *
     * @param id
     * @return
     */
    GroupInfoDto getGroupCountByGroupId(Long id);

    /**
     * 关键字查询组
     *
     * @param word
     * @return
     */
    List<GroupInfoDto> findGroupBySearch(String word);

    /**
     * 我申请的群组
     *
     * @param memberId
     * @return
     */
    List<GroupInfoDto> mineApplyGroup(Long memberId);

    /**
     * 申请我的群组
     *
     * @param memberId
     * @return
     */
    List<GroupInfoDto> applyMineGroup(Long memberId);

    /**
     * 添加组
     *
     * @param memberId
     * @param name
     * @param imageUrl
     */
    void addGroup(Long memberId, String name, String imageUrl);

}