/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OauthDetailsDto
 * Author:   Derek Xu
 * Date:     2022/9/2 13:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
@Data
public class OAuthDetailsDto implements Serializable {
    /**
     * 客户端ID
     */
    @Schema(title = "客户端id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @Schema(title = "客户端密钥")
    private String clientSecret;

    /**
     * 资源ID
     */
    private String resourceIds;

    /**
     * 作用域
     */
    @Schema(title = "作用域")
    private String scope;

    /**
     * 授权方式（A,B,C）
     */
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    private String webServerRedirectUri;

    /**
     * 权限
     */
    private String authorities;

    /**
     * 请求令牌有效时间
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间
     */
    private Integer refreshTokenValidity;

    /**
     * 扩展信息
     */
    private String additionalInformation;

    /**
     * 是否自动放行
     */
    private String autoapprove;
}