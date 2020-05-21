package com.auv.kill.model.mapper;

import com.auv.kill.model.entity.RandomCode;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (RandomCode)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
public interface RandomCodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RandomCode queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<RandomCode> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param randomCode 实例对象
     * @return 对象列表
     */
    List<RandomCode> queryAll(RandomCode randomCode);

    /**
     * 新增数据
     *
     * @param randomCode 实例对象
     * @return 影响行数
     */
    int insert(RandomCode randomCode);

    /**
     * 修改数据
     *
     * @param randomCode 实例对象
     * @return 影响行数
     */
    int update(RandomCode randomCode);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}