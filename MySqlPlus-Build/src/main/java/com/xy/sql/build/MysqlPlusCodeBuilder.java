package com.xy.sql.build;

import com.xy.sql.build.kit.XmlBuilder;
import com.xy.sql.build.vo.Column;
import com.xy.sql.build.vo.TableVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成
 *
 * @author liubq
 * @since 2018年1月4日
 */
public class MysqlPlusCodeBuilder {


    /**
     * 创建
     *
     * @param conn
     * @param table
     * @throws Exception
     */
    public static String build(Connection conn, String table) throws Exception {
        TableVO vo = parse(conn, table);
        return XmlBuilder.build(vo);
    }

    /**
     * 查询所有列
     *
     * @param conn
     * @param table
     * @return
     * @throws Exception
     */
    private static TableVO parse(Connection conn, String table) throws Exception {
        TableVO vo = new TableVO();
        vo.setTable(table);
        String sql = " select * from " + table + " where 1=2 ";
        PreparedStatement stmt = null;

        try {
            List<Column> list = new ArrayList<>();
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            for (int i = 1; i <= data.getColumnCount(); i++) {
                Column c = new Column();
                c.setName(data.getColumnName(i));
                c.setDataType(data.getColumnTypeName(i).toUpperCase());
                list.add(c);
            }
            vo.setCols(list);
            ResultSet pkRs = null;
            try {
                // 适用mysql
                pkRs = conn.getMetaData().getPrimaryKeys(conn.getCatalog().toUpperCase(), null, table.toUpperCase());
                // 适用oracle,mysql
                List<String> pkList = new ArrayList<String>();
                while (pkRs.next()) {
                    pkList.add(pkRs.getString("COLUMN_NAME"));
                }
                vo.setPks(pkList);
            } finally {
                if (pkRs != null) {
                    try {
                        pkRs.close();
                    } catch (Exception e) {
                    }
                }
            }


        } finally {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        }
        return vo;
    }

}
