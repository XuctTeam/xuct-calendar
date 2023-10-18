/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: WxMaServiceImpl
 * Author:   Derek Xu
 * Date:     2023/2/3 17:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.*;
import cn.com.xuct.calendar.basic.services.config.WxMaConfiguration;
import cn.com.xuct.calendar.basic.services.service.WxMaService;
import cn.com.xuct.calendar.common.module.feign.req.WxSubscribeMessageFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserPhoneFeignInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/2/3
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxMaServiceImpl implements WxMaService {

    private final WxMaConfiguration wxMaConfiguration;

    @Override
    public WxMaJscode2SessionResult getSessionInfo(String code) throws WxErrorException {
        return wxMaConfiguration.getMaService().getUserService().getSessionInfo(code);
    }
    @Override
    public WxMaUserInfo getUserInfo(final String sessionKey, final String encryptedData , final String iv) {
           return wxMaConfiguration.getMaService().getUserService().getUserInfo(sessionKey, encryptedData, iv);
    }
    @Override
    public WxMaPhoneNumberInfo getPhoneNoInfo(final String code) throws WxErrorException {
        return  wxMaConfiguration.getMaService().getUserService().getNewPhoneNoInfo(code);
    }
    @Override
    public void sendSubscribeMsg(List<WxSubscribeMessageFeignInfo> subscribeMessageFeignInfo) {
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
    }

    @Override
    public String getQrCode(String scene, String page, String envVersion, Integer width) throws WxErrorException {
        WxMaQrcodeService wxMaQrcodeService = wxMaConfiguration.getMaService().getQrcodeService();
        byte[] qrBy = wxMaQrcodeService.createWxaCodeUnlimitBytes(scene, page, true, envVersion, width, true, new WxMaCodeLineColor("255", "255", "255"), true);
        return new String(qrBy, StandardCharsets.UTF_8);
    }
}