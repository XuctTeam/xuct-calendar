/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: OAuthClientDetailsController
 * Author:   Derek Xu
 * Date:     2022/9/2 15:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.ums.api.entity.OAuthClientDetails;
import cn.com.xuct.calendar.ums.boot.service.IOAuthClientDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/2
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【远程调用】客户端管理接口")
@RequestMapping("/api/feign/v1/oauth/client")
@RequiredArgsConstructor
public class OAuthClientDetailsController {


    private final IOAuthClientDetailsService authClientDetailsService;

    /**
     * 通过ID查询
     *
     * @param clientId 客户端id
     * @return SysOauthClientDetails
     */
    @Inner
    @GetMapping("/{clientId}")
    public R<OAuthClientDetails> getByClientId(@PathVariable String clientId) {
        return R.data(authClientDetailsService.get(Column.of("client_id", clientId)));
    }
}