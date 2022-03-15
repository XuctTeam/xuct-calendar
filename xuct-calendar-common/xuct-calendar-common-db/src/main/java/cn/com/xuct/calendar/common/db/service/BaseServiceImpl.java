/**
 * Copyright (C), 2015-2019, xuct.net
 * FileName: BaseServiceImpl
 * Author:   xutao
 * Date:     2019/12/27 14:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.db.service;


import cn.com.xuct.calendar.common.core.enums.ColumnEnum;
import cn.com.xuct.calendar.common.core.enums.SortEnum;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.core.vo.Sort;
import cn.com.xuct.calendar.common.db.dao.base.SuperEntity;
import cn.com.xuct.calendar.common.db.dao.vo.PageData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author xutao
 * @create 2019/12/27
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class BaseServiceImpl<M extends BaseMapper<T>, T extends SuperEntity<T>> extends ServiceImpl<BaseMapper<T>, T> implements IBaseService<M, T> {


    @Override
    protected Class<BaseMapper<T>> currentMapperClass() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<BaseMapper<T>>) type.getActualTypeArguments()[0];
    }

    /**
     * 获取对象
     *
     * @param entity
     * @return
     */
    @Override
    public T get(T entity) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        queryWrapper.setEntity(entity);
        return this.getOne(queryWrapper);
    }

    /**
     * 获取对象
     *
     * @param column
     * @return
     */
    @Override
    public T get(Column column) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        setWrapper(queryWrapper, column);
        return this.getOne(queryWrapper);
    }

    @Override
    public T get(List<Column> columns) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        columns.stream().forEach(column -> setWrapper(queryWrapper, column));
        return this.getOne(queryWrapper);
    }


    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param entity
     * @return:java.util.List<T>
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 16:20
     */
    @Override
    public List<T> list(T entity) {
        return list(entity, null);
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param entity
     * @param sort
     * @return:java.util.List<T>
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 16:36
     */
    @Override
    public List<T> list(T entity, Sort sort) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        if (entity != null) {
            queryWrapper.setEntity(entity);
        }
        if (sort != null) {
            setSort(queryWrapper, sort);
        }
        return this.getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param column
     * @return:java.util.List<T>
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 16:36
     */
    @Override
    public List<T> find(Column column) {
        return this.find(Lists.newArrayList(column));
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param columns
     * @return:java.util.List<T>
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 20:32
     */
    @Override
    public List<T> find(List<Column> columns) {
        return this.find(columns, null);
    }


    @Override
    public List<T> find(List<Column> columns, Sort sort) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        if (!CollectionUtils.isEmpty(columns)) {
            columns.stream().forEach(column -> setWrapper(queryWrapper, column));
        }
        if (sort != null) {
            this.setSort(queryWrapper, sort);
        }
        return this.getBaseMapper().selectList(queryWrapper);
    }


    @Override
    public long count(Column colum) {
        return this.count(Lists.newArrayList(colum));
    }

    /**
     * 功能描述: <br>
     * 〈统计总数〉
     *
     * @param columns
     * @return:java.lang.Long
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 21:48
     */
    @Override
    public long count(List<Column> columns) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        columns.stream().forEach(column -> setWrapper(queryWrapper, column));
        return this.count(queryWrapper);
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param page
     * @return:com.net263.vcs.api.server.common.vo.PageData<T>
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 20:43
     */
    public IPage<T> pages(IPage<T> page) {
        return pages(page, null);
    }

    /**
     * 通过分页查询
     *
     * @param page
     * @param column
     * @return
     */
    @Override
    public IPage<T> pages(IPage<T> page, Column column) {
        return pages(page, column, null);
    }

    /**
     * 通过分页查询
     *
     * @param page
     * @param column
     * @param sort
     * @return
     */
    @Override
    public IPage<T> pages(IPage<T> page, Column column, Sort sort) {
        return pages(page, column != null ? Lists.newArrayList(column) : Lists.newArrayList(), sort);
    }

    @Override
    public IPage<T> pages(IPage<T> page, List<Column> columns, Sort sort) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        if (!CollectionUtils.isEmpty(columns)) {
            columns.stream().forEach(column -> setWrapper(queryWrapper, column));
        }
        if (sort != null) {
            setSort(queryWrapper, sort);
        }
        return this.getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    public void delete(Column column) {
        QueryWrapper<T> queryWrapper = this.getQuery();
        setWrapper(queryWrapper, column);
        this.remove(queryWrapper);
    }

    @Override
    public void update(T entity, Column column) {
        this.update(entity, Lists.newArrayList(column));
    }

    @Override
    public void update(T entity, List<Column> columns) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        if (!CollectionUtils.isEmpty(columns)) {
            columns.stream().forEach(column -> setUpWrapper(updateWrapper, column));
        }
        this.update(entity, updateWrapper);
    }

    @Override
    public QueryWrapper<T> getQuery() {
        return new QueryWrapper<T>();
    }

    @Override
    public UpdateWrapper<T> getUpdate() {
        return new UpdateWrapper<T>();
    }


    @Override
    public PageData<T> convert(IPage<T> pageMode) {
        return new PageData().put(pageMode);
    }


    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param queryWrapper
     * @param column
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 17:33
     */
    private void setWrapper(QueryWrapper<T> queryWrapper, Column column) {
        if (column.getColumnEnum().equals(ColumnEnum.eq)) {
            queryWrapper.eq(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.nq)) {
            queryWrapper.ne(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.in)) {
            queryWrapper.in(column.getColumn(), column.getValue().toArray());
        }
        if (column.getColumnEnum().equals(ColumnEnum.like)) {
            queryWrapper.like(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.like_left)) {
            queryWrapper.likeLeft(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.like_right)) {
            queryWrapper.likeRight(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.lt)) {
            queryWrapper.lt(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.le)) {
            queryWrapper.le(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.gt)) {
            queryWrapper.gt(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.ge)) {
            queryWrapper.ge(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumn().equals(ColumnEnum.is_not_null)) {
            queryWrapper.isNotNull(column.getColumn());
        }
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param upWrapper
     * @param column
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/3/16 11:00
     */
    private void setUpWrapper(UpdateWrapper<T> upWrapper, Column column) {
        if (column.getColumnEnum().equals(ColumnEnum.eq)) {
            upWrapper.eq(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.nq)) {
            upWrapper.ne(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.in)) {
            upWrapper.in(column.getColumn(), column.getValue());
        }
        if (column.getColumnEnum().equals(ColumnEnum.like)) {
            upWrapper.like(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.like_left)) {
            upWrapper.likeLeft(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.like_right)) {
            upWrapper.likeRight(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.lt)) {
            upWrapper.lt(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.le)) {
            upWrapper.le(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.gt)) {
            upWrapper.gt(column.getColumn(), Lists.newArrayList().get(0));
        }
        if (column.getColumnEnum().equals(ColumnEnum.ge)) {
            upWrapper.ge(column.getColumn(), Lists.newArrayList(column.getValue()).get(0));
        }
        if (column.getColumn().equals(ColumnEnum.is_not_null)) {
            upWrapper.isNotNull(column.getColumn());
        }
        if (column.getColumnEnum().equals(ColumnEnum.is_null)) {
            upWrapper.isNull(column.getColumn());
        }
    }

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param queryWrapper
     * @param sort
     * @return:void
     * @since: 1.0.0
     * @Author:
     * @Date: 2020/2/3 20:54
     */
    private void setSort(QueryWrapper<T> queryWrapper, Sort sort) {
        if (sort.getSortEnum().equals(SortEnum.asc)) {
            queryWrapper.orderByAsc(sort.getColumn());
        } else {
            queryWrapper.orderByDesc(sort.getColumn());
        }
    }
}