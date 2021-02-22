package com.xy.sql.build.vo;

import com.xy.sql.build.kit.NameUtil;

import java.util.List;

/**
 * 表信息
 */
public class TableVO {
    //属性
    private List<Column> cols;
    //表名
    private String table;
    //主键
    private List<String> pks;
    //主键名称
    private String pk = null;
    //主键名称
    private String pkName = null;


    public List<Column> getCols() {
        return cols;
    }

    public void setCols(List<Column> cols) {
        this.cols = cols;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getPks() {
        return pks;
    }

    public void setPks(List<String> pks) {
        this.pks = pks;
    }

}
