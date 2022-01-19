/**
 * Copyright (C), 2015-2019, xuct.net
 * FileName: PageDatga
 * Author:   xutao
 * Date:     2019/12/27 16:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.dao.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author xutao
 * @create 2019/12/27
 * @since 1.0.0
 */
public class PageData<T> {

    //时间戳
    private Long timestamp = System.currentTimeMillis();

    private Long pageTotal;

    //总条数
    private long total;

    //数据
    private List<T> list = Lists.newArrayList();

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param page
     * @return:com.net263.vcs.api.server.common.vo.PageData
     * @since: 1.0.0
     * @Author:
     * @Date: 2019/12/27 16:40
     */
    public PageData put(IPage page) {
        this.setList(page.getRecords());
        this.setPageTotal(page.getPages());
        this.setTotal(page.getTotal());
        return this;
    }

    /**
     * @param list
     * @return
     */
    public PageData put(List<T> list) {
        this.setList(list);
        this.setPageTotal(1L);
        this.setTotal(list.size());
        return this;
    }
}