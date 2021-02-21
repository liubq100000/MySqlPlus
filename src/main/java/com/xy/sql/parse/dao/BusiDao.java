package com.xy.sql.parse.dao;

import com.xy.sql.parse.MySqlPlus;
import com.xy.sql.parse.kit.MySqlStandard;
import com.xy.sql.parse.vo.SqlVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dao操作
 */
public class BusiDao {


    /**
     * 执行
     *
     * @param conn
     * @param sqlId
     * @param paraMap
     * @throws Exception
     */
    public static void execute(Connection conn, String sqlId, Map<String, Object> paraMap) throws Exception {
        // 取得连接
        PreparedStatement stmt = null;
        try {
            // 解析SQL
            SqlVO vo = MySqlPlus.buildSql(sqlId, paraMap);
            if (vo == null) {
                throw new Exception("指定ID" + sqlId + "不存在！");
            }
            //打印SQL
            MySqlStandard.printSql(vo);
            stmt = conn.prepareStatement(vo.getSql());
            for (int i = 0; i < vo.getVoList().size(); i++) {
                stmt.setObject(i + 1, vo.getVoList().get(i));
            }
            stmt.execute();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询所有列
     *
     * @param conn
     * @param sqlId
     * @param paraMap
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> queryForList(Connection conn, String sqlId, Map<String, Object> paraMap) throws Exception {
        // 取得连接
        PreparedStatement stmt = null;
        try {
            // 解析SQL
            SqlVO vo = MySqlPlus.buildSql(sqlId, paraMap);
            if (vo == null) {
                throw new Exception("指定ID" + sqlId + "不存在！");
            }
            //打印SQL
            MySqlStandard.printSql(vo);
            stmt = conn.prepareStatement(vo.getSql());
            for (int i = 0; i < vo.getVoList().size(); i++) {
                stmt.setObject(i + 1, vo.getVoList().get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData data = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    itemMap.put(data.getColumnLabel(i), rs.getObject(i));
                }
                list.add(itemMap);
            }
            return MySqlPlus.convert(sqlId, list);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


//    /**
//     * 分页查询
//     *
//     * @param sql
//     * @param paraMap
//     * @param inPage
//     * @return
//     * @throws Exception
//     */
//    public PageManagerEx<Map<String, Object>> queryByPage(String sql, Map<String, Object> paraMap, PageManager inPage) throws Exception {
//        // 取得连接
//        PreparedStatement stmtCount = null;
//        PreparedStatement stmtQuery = null;
//        ResultSet rsCount = null;
//        ResultSet rsQuery = null;
//        try {
//            PageManagerEx<Map<String, Object>> resPage = new PageManagerEx<Map<String, Object>>();
//            resPage.setPageRows(inPage.getPageRows());
//            if (resPage.getPageRows() < 0) {
//                resPage.setPageRows(10);
//            }
//            resPage.setPage(inPage.getPage());
//            if (resPage.getPage() < 0) {
//                resPage.setPage(0);
//            }
//            // 解析SQL
//            SqlVO vo = SqlProcess.action(sql, paraMap);
//            // 分析出数量和列表查询语句
//            String countSql = SqlParseTool.pageCountSql(vo.getSql());
//            String querySql = SqlParseTool.pageQuerySql(vo.getSql());
//            System.out.println(countSql);
//            stmtCount = conn.prepareStatement(countSql);
//            Object value;
//            StringBuilder sqlPara = new StringBuilder();
//            for (int i = 0; i < vo.getVoList().size(); i++) {
//                value = vo.getVoList().get(i);
//                sqlPara.append(value).append(" ");
//                stmtCount.setObject(i + 1, value);
//            }
//            System.out.println(sqlPara.toString());
//            rsCount = stmtCount.executeQuery();
//            Integer count = null;
//            if (rsCount.next()) {
//                count = rsCount.getInt(1);
//            }
//            resPage.setTotalCount(count == null ? 0 : count);
//
//            int totalPages = count / resPage.getPageRows();
//            if (count % resPage.getPageRows() > 0) {
//                totalPages++;
//            }
//            resPage.setTotalPages(totalPages);
//
//            if (resPage.getPage() >= totalPages) {
//                resPage.setPage(totalPages - 1);
//            }
//            if (resPage.getPage() < 0) {
//                resPage.setPage(0);
//            }
//            StringBuilder limit = new StringBuilder();
//            limit.append(" limit ").append(resPage.getPage() * resPage.getPageRows()).append(",").append(resPage.getPageRows()).append(" ");
//            System.out.println(querySql + limit.toString());
//            stmtQuery = conn.prepareStatement(querySql + limit.toString());
//            StringBuilder sqlParaQuery = new StringBuilder();
//            for (int i = 0; i < vo.getVoList().size(); i++) {
//                value = vo.getVoList().get(i);
//                sqlParaQuery.append(value).append(" ");
//                stmtQuery.setObject(i + 1, value);
//            }
//            System.out.println(sqlParaQuery.toString());
//            rsQuery = stmtQuery.executeQuery();
//            ResultSetMetaData data = rsQuery.getMetaData();
//            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//            while (rsQuery.next()) {
//                Map<String, Object> itemMap = new HashMap<String, Object>();
//                for (int i = 1; i <= data.getColumnCount(); i++) {
//                    itemMap.put(data.getColumnLabel(i), rsQuery.getObject(i));
//                }
//                list.add(itemMap);
//            }
//            resPage.setData(list);
//            return resPage;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            if (rsCount != null) {
//                try {
//                    rsCount.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (rsQuery != null) {
//                try {
//                    rsQuery.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stmtCount != null) {
//                try {
//                    stmtCount.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (stmtQuery != null) {
//                try {
//                    stmtQuery.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


}
