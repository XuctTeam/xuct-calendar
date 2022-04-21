/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ClientDetailsService
 * Author:   Derek Xu
 * Date:     2021/11/15 8:55
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.core.clientdetails;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */
@Slf4j
public class AuthClientDetailsService extends JdbcClientDetailsService {

    public AuthClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 重写原生方法支持redis缓存
     *
     * @param clientId
     * @return
     */
    @Override
    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            ClientDetails clientDetails = super.loadClientByClientId(clientId);
            return clientDetails;
        } catch (Exception ee) {
            log.info("auth client:: get client by id error: reason: client not exist , id = {}", clientId);
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }
}