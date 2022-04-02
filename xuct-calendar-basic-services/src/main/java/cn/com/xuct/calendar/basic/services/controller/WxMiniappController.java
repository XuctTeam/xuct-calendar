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
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.basic.services.config.WxMaConfiguration;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.WxSubscribeMessageFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserPhoneFeignInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "【基础服务】微信接口")
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

    @ApiOperation(value = "发送订阅消息")
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
}