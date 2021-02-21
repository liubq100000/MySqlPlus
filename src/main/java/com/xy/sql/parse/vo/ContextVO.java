package com.xy.sql.parse.vo;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.node.MyMapperNode;

import java.util.Map;

/**
 * 执行上下文
 */
public class ContextVO {

    //Mapper
    private MyMapperNode mapper;

    /**
     * 初始化
     *
     * @param mapper
     */
    public ContextVO(MyMapperNode mapper) {
        this.mapper = mapper;
    }

    /**
     * 取得Mapper
     *
     * @return
     */
    public MyMapperNode getMapper() {
        return mapper;
    }

    //模型
    private Map<String, Map<String, String>> itemMap;



    /**
     * 取得指定节点
     *
     * @param id
     * @return
     */
    public MySqlPlusNode getSqlFragmentById(String id) {
        return mapper.getSqlSegmentById(id);
    }

    /**
     * 取得模型信息
     *
     * @return
     */
    public Map<String, Map<String, String>> getItemMap() {
        return mapper.getItemMap();
    }
}
