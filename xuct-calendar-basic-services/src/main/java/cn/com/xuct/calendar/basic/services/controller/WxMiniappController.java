/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: WxMiniappController
 * Author:   Derek Xu
 * Date:     2022/3/29 15:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.basic.services.config.WxMaConfiguration;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.module.feign.WxUserPhoneFeignInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/29
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【基础服务】短信接口")
@RequiredArgsConstructor
@RequestMapping("/api/basic/v1/wx/miniapp")
public class WxMiniappController {

    private final WxMaConfiguration wxMaConfiguration;

    @ApiOperation(value = "获取登陆SESSION")
    @GetMapping("/getSessionInfo")
    public R<WxMaJscode2SessionResult> getSessionInfo(@RequestParam("code") String code) throws WxErrorException {
        WxMaJscode2SessionResult session = wxMaConfiguration.getMaService().getUserService().getSessionInfo(code);
        return session == null ? R.fail("查询session失败") : R.data(session);
    }

    @ApiOperation(value = "获取登录用户")
    @PostMapping
    public R<WxMaUserInfo> getUserInfo(@Validated @RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo) {
        Assert.notNull(wxUserInfoFeignInfo.getSessionKey(), "sessionKey must be empty");
        WxMaUserInfo wxMaUserInfo = wxMaConfiguration.getMaService().getUserService().getUserInfo(wxUserInfoFeignInfo.getSessionKey(), wxUserInfoFeignInfo.getEncryptedData(), wxUserInfoFeignInfo.getIv());
        return wxMaUserInfo == null ? R.fail("查询微信失败") : R.data(wxMaUserInfo);
    }

    @ApiOperation(value = "获取用户电话")
    @PostMapping("/getPhoneNoInfo")
    public R<WxMaPhoneNumberInfo> getPhoneNoInfo(@Validated @RequestBody WxUserPhoneFeignInfo wxUserPhoneFeignInfo) {
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaConfiguration.getMaService().getUserService().getPhoneNoInfo(wxUserPhoneFeignInfo.getSessionKey(), wxUserPhoneFeignInfo.getEncryptedData(), wxUserPhoneFeignInfo.getIvStr());
        return wxMaPhoneNumberInfo == null ? R.fail("查询微信失败") : R.data(wxMaPhoneNumberInfo);
    }
}