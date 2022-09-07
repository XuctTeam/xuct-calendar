/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OAuthAuthorization
 * Author:   Derek Xu
 * Date:     2022/9/7 18:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.security.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/7
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAuthorization implements Serializable {


    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;


}