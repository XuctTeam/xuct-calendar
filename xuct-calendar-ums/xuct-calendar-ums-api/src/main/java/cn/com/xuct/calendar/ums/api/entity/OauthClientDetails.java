/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OauthClientDetails
 * Author:   Derek Xu
 * Date:     2022/9/2 11:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.api.entity;

import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_oauth_client_details")
public class OauthClientDetails extends SuperEntity<OauthClientDetails> {

    /**
     * 客户端ID
     */
    @NotBlank(message = "client_id 不能为空")
    @Schema(title = "客户端id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "client_secret 不能为空")
    @Schema(title = "客户端密钥")
    private String clientSecret;

    /**
     * 资源ID
     */
    @Schema(title = "资源id列表")
    private String resourceIds;

    /**
     * 作用域
     */
    @NotBlank(message = "scope 不能为空")
    @Schema(title = "作用域")
    private String scope;

    /**
     * 授权方式（A,B,C）
     */
    @Schema(title = "授权方式")
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @Schema(title = "回调地址")
    private String webServerRedirectUri;

    /**
     * 权限
     */
    @Schema(title = "权限列表")
    private String authorities;

    /**
     * 请求令牌有效时间
     */
    @Schema(title = "请求令牌有效时间")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间
     */
    @Schema(title = "刷新令牌有效时间")
    private Integer refreshTokenValidity;

    /**
     * 扩展信息
     */
    @Schema(title = "扩展信息")
    private String additionalInformation;

    /**
     * 是否自动放行
     */
    @Schema(title = "是否自动放行")
    private String autoapprove;
}