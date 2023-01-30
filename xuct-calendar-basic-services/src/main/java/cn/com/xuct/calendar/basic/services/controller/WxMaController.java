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

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.*;
import cn.com.xuct.calendar.basic.services.config.WxMaConfiguration;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.WxSubscribeMessageFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserPhoneFeignInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
@Tag(name = "【基础服务】微信小程序接口")
@RequiredArgsConstructor
@RequestMapping("/api/basic/v1/wx/ma")
public class WxMaController {

    private final WxMaConfiguration wxMaConfiguration;

    @Operation(summary = "获取登陆SESSION")
    @GetMapping("/getSessionInfo")
    public R<WxMaJscode2SessionResult> getSessionInfo(@RequestParam("code") String code) throws WxErrorException {
        WxMaJscode2SessionResult session = wxMaConfiguration.getMaService().getUserService().getSessionInfo(code);
        return session == null ? R.fail("查询session失败") : R.data(session);
    }

    @Operation(summary = "获取登录用户")
    @PostMapping("/getUserInfo")
    public R<WxMaUserInfo> getUserInfo(@Validated @RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo) {
        Assert.notNull(wxUserInfoFeignInfo.getSessionKey(), "sessionKey must be empty");
        WxMaUserInfo wxMaUserInfo = wxMaConfiguration.getMaService().getUserService().getUserInfo(wxUserInfoFeignInfo.getSessionKey(), wxUserInfoFeignInfo.getEncryptedData(), wxUserInfoFeignInfo.getIv());
        return wxMaUserInfo == null ? R.fail("查询微信失败") : R.data(wxMaUserInfo);
    }

    @Operation(summary = "获取用户电话")
    @PostMapping("/getPhoneNoInfo")
    public R<WxMaPhoneNumberInfo> getPhoneNoInfo(@Validated @RequestBody WxUserPhoneFeignInfo wxUserPhoneFeignInfo) throws WxErrorException {
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxMaConfiguration.getMaService().getUserService().getNewPhoneNoInfo(wxUserPhoneFeignInfo.getCode());
        return wxMaPhoneNumberInfo == null ? R.fail("查询微信失败") : R.data(wxMaPhoneNumberInfo);
    }

    @Operation(summary = "发送订阅消息")
    @PostMapping("/sendSubscribeMsg")
    public R<String> sendSubscribeMsg(@Validated @RequestBody List<WxSubscribeMessageFeignInfo> subscribeMessageFeignInfo) {
        WxMaMsgService wxMaMsgService = wxMaConfiguration.getMaService().getMsgService();
        subscribeMessageFeignInfo.stream().forEach(wxSubscribeReq -> {
            WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
            BeanUtils.copyProperties(wxSubscribeReq, wxMaSubscribeMessage);
            if (!CollectionUtils.isEmpty(wxSubscribeReq.getData())) {
                wxMaSubscribeMessage.setData(wxSubscribeReq.getData().stream().map(x -> {
                    return new WxMaSubscribeMessage.MsgData(x.getName(), x.getName());
                }).collect(Collectors.toList()));
            }
            try {
                wxMaMsgService.sendSubscribeMsg(wxMaSubscribeMessage);
            } catch (WxErrorException e) {
                log.error("wx miniapp controller:: send subscribe message error , to user = {}", wxSubscribeReq.getToUser());
            }
        });
        return R.status(true);
    }

    @Operation(summary = "获取小程序码")
    @GetMapping("/qrcode")
    public R<String> getMaQrCode(@RequestParam("scene") String scene, @RequestParam("page") String page, @RequestParam("envVersion") String envVersion, @RequestParam("width") Integer width) throws WxErrorException {
        WxMaQrcodeService wxMaQrcodeService = wxMaConfiguration.getMaService().getQrcodeService();
        byte[] qrBy = wxMaQrcodeService.createWxaCodeUnlimitBytes(scene, page, true, envVersion, width, true, new WxMaCodeLineColor("255", "255", "255"), true);
        return R.data(new String(qrBy, StandardCharsets.UTF_8));
    }
}