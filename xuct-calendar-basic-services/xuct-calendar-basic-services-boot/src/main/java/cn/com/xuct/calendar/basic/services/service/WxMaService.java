/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: WxMaService
 * Author:   Derek Xu
 * Date:     2023/2/3 17:28
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxSubscribeMessageFeignInfo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/2/3
 * @since 1.0.0
 */
public interface WxMaService {

    /**
     * 取小程序session
     * @param code
     * @return
     * @throws WxErrorException
     */
    WxMaJscode2SessionResult getSessionInfo(final String code) throws WxErrorException;

    /**
     * 获取用户信息
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    WxMaUserInfo getUserInfo(final String sessionKey, final String encryptedData , final String iv);

    /**
     * 获取小程序手机号
     * @param code
     * @return
     * @throws WxErrorException
     */
    WxMaPhoneNumberInfo getPhoneNoInfo(final String code) throws WxErrorException;
    /**
     * 发送小程序通知消息
     * @param subscribeMessageFeignInfo
     */
    void sendSubscribeMsg(final List<WxSubscribeMessageFeignInfo> subscribeMessageFeignInfo);

    /**
     * 获取小程序分享二维码
     * @param scene
     * @param page
     * @param envVersion
     * @param width
     * @return
     */
    String getQrCode(final String scene, final String page, final String envVersion, final Integer width) throws WxErrorException;
}