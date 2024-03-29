/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.xuct.calendar.common.module.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lengleng
 * @date 2019/2/1
 * <p>
 * commit('SET_ROLES', data) commit('SET_NAME', data) commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data) commit('SET_PERMISSIONS', data)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

	/**
	 * 为系统用户时为 user 会员时为member
	 */
	private PersonInfo personInfo;

	/**
	 * 权限标识集合
	 */
	private String[] permissions;

	/**
	 * 角色集合
	 */
	private Long[] roles;

	/**
	 * 角色集合
	 */
	private List<UserRoleInfo> roleList;
}
