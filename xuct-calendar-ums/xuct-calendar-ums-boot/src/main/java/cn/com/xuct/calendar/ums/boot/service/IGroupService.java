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

import cn.com.xuct.calendar.common.db.service.IBaseService;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.api.vo.GroupMemberTreeVo;
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

    List<GroupInfoDto> listGroupCountByMember(final Long memberId);

    List<GroupMemberTreeVo> listGroupTree(final Long memberId);

    GroupInfoDto getGroupCountByGroupId(final Long id);

    List<GroupInfoDto> pageGroupBySearch(final Long memberId, final String word, final Integer page, final Integer limit, final String hasPass, final String dateScope, final String numCount);

    List<GroupMemberInfoDto> mineApplyGroup(Long memberId);

    List<GroupMemberInfoDto> applyMineGroup(Long memberId);

    void addGroup(final Long memberId, final String name, final String password, final String imageUrl, final String power, final Integer num, final String no);

    List<Long> deleteGroup(Long id);

    void removeAllGroupByMemberId(final Long memberId);
}