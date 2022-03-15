/**
 * Copyright (C), 2015-2019, xuct.net
 * FileName: BaseService
 * Author:   xutao
 * Date:     2019/12/27 14:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.db.service;


import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.core.vo.Sort;
import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import cn.com.xuct.calendar.common.db.dao.vo.PageData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author xutao
 * @create 2019/12/27
 * @since 1.0.0
 */
public interface IBaseService<M extends BaseMapper<T>, T extends SuperEntity> extends IService<T> {

    /**
     * 获取对象
     *
     * @param entity
     * @return
     */
    T get(T entity);


    /**
     * 获取对象
     *
     * @param column
     * @return
     */
    T get(Column column);

    /**
     * 查询对象
     *
     * @param columns
     * @return
     */
    T get(List<Column> columns);

    /**
     * 通过条件查询所有列表
     *
     * @param entity
     * @return
     */
    List<T> list(T entity);


    /**
     * 通过条件查询并排序列表
     *
     * @param entity
     * @param sort
     * @return
     */
    List<T> list(T entity, Sort sort);

    /**
     * 通过字段查询
     *
     * @param column
     * @return
     */
    List<T> find(Column column);

    /**
     * 通过一些字段查询
     *
     * @param columns
     * @return
     */
    List<T> find(List<Column> columns);


    /**
     * 通过关键词查询
     *
     * @param columns
     * @param sort
     * @return
     */
    List<T> find(List<Column> columns, Sort sort);


    /**
     * 统计总数
     *
     * @param colum
     * @return
     */
    long count(Column colum);


    /**
     * 统计总数
     *
     * @param columns
     * @return
     */
    long count(List<Column> columns);


    /**
     * 分页
     *
     * @param pageable
     * @return
     */
    IPage<T> pages(IPage<T> pageable);

    /**
     * 通过分页查询
     *
     * @param page
     * @param column
     * @return
     */
    IPage<T> pages(IPage<T> page, Column column);


    /**
     * 通过分页查询
     *
     * @param page
     * @param column
     * @param sort
     * @return
     */
    IPage<T> pages(IPage<T> page, Column column, Sort sort);


    /**
     * 分页统计
     *
     * @param page
     * @param columns
     * @param sort
     * @return
     */
    IPage<T> pages(IPage<T> page, List<Column> columns, Sort sort);


    /**
     * 转换返回数据
     *
     * @param pageMode
     * @return
     */
    PageData<T> convert(IPage<T> pageMode);

    /**
     * 删除
     *
     * @param column
     */
    void delete(Column column);


    /**
     * 更新
     *
     * @param column
     */
    void update(T entity, Column column);


    /**
     * 更新
     *
     * @param entity
     * @param columns
     */
    void update(T entity, List<Column> columns);

    /**
     * 获取查询条件
     *
     * @return
     */
    QueryWrapper<T> getQuery();


    /**
     *
     * @return
     */
    UpdateWrapper<T> getUpdate();
}