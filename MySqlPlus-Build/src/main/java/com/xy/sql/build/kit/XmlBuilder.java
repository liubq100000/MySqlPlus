package com.xy.sql.build.kit;

import com.xy.sql.build.vo.Column;
import com.xy.sql.build.vo.TableVO;

import java.util.ArrayList;
import java.util.List;

public class XmlBuilder {

    public static String build(TableVO vo) throws Exception {

        List<Column> list = vo.getCols();
        String table = vo.getTable().toLowerCase();
        StringBuilder s = new StringBuilder();
        s.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
//        s.append("<!DOCTYPE mapper").append("\r\n");
//        s.append("PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"").append("\r\n");
//        s.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").append("\r\n");
        s.append("<mapper namespace=\"" + table + "_mapper\">").append("\r\n");
        s.append(modelSql(list, table));
        s.append("\r\n");
        s.append(insertSql(list, table));
        s.append("\r\n");
        s.append(updateSql(list, table, vo.getPks()));
        s.append("\r\n");
        s.append(deleteSql(table, vo.getPks()));
        s.append("\r\n");
        s.append(fromWhereSql(list, table));
        s.append("\r\n");
        s.append(querySql(list));
        s.append("\r\n");
        s.append(countSql());
        s.append("\r\n");
        s.append("</mapper>").append("\r\n");
        return s.toString();
    }

    private static String insertSql(List<Column> list, String table) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <insert id=\"insert\" parameterType=\"Map\">").append("\r\n");
        String name;
        s.append("    INSERT INTO " + table + "(");
        Column c = null;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            name = c.getName().toUpperCase();
            if (i > 0) {
                s.append(",");
            }
            s.append(name);
        }
        s.append(" ) ").append("\r\n");
        s.append("     VALUES (");
        c = null;
        String fieldName;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            fieldName = NameUtil.fieldName(c.getName());
            if (i > 0) {
                s.append(",");
            }
            s.append("#{" + fieldName + "}");

        }
        s.append(" ) ").append("\r\n");
        s.append("  </insert> ").append("\r\n");
        return s.toString();
    }

    private static String updateSql(List<Column> list, String table, List<String> inPks) throws Exception {
        List<String> pks = toUp(inPks);
        StringBuilder s = new StringBuilder();
        s.append("  <update id=\"update\" parameterType=\"Map\">").append("\r\n");

        s.append("      UPDATE " + table + "").append("\r\n");
        s.append("      <set> ").append("\r\n");
        String name;
        String fieldName;
        Column c;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            name = c.getName().toUpperCase();
            if (pks.contains(name)) {
                continue;
            }
            fieldName = NameUtil.fieldName(c.getName());
            s.append("          <if test=\"" + fieldName + " != null\"> ").append("\r\n");
            s.append("              " + name + " = #{" + fieldName + "}").append(",\r\n");
            s.append("          </if> ").append("\r\n");
        }
        s.append("      </set> ").append("\r\n");
        s.append("      where ").append(pkSql(inPks)).append("\r\n");
        s.append("  </update> ").append("\r\n");
        return s.toString();
    }

    private static List<String> toUp(List<String> pks) {
        List<String> newList = new ArrayList<>();
        for (String pk : pks) {
            newList.add(pk.toUpperCase());
        }
        return newList;
    }

    private static String pkSql(List<String> pks) {
        StringBuilder sql = new StringBuilder();
        for (String pk : pks) {
            String idName = NameUtil.fieldName(pk);
            if (sql.length() > 1) {
                sql.append(" and ");
            }
            sql.append(" ").append(idName).append("=").append("#{").append(idName).append("} ");
        }
        return sql.toString();
    }

    private static String deleteSql(String table,List<String> inPks) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <delete id=\"delete\" parameterType=\"Map\">").append("\r\n");
        s.append("    delete from " + table + " ").append(" where ").append(pkSql(inPks)).append("\r\n");
        s.append("  </delete>").append("\r\n");
        return s.toString();
    }

    private static String modelSql(List<Column> list, String table) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <model id=\"" + table + "_model\">").append("\r\n");

        String name;
        String fieldName;
        Column c;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            name = c.getName().toUpperCase();
            fieldName = NameUtil.fieldName(c.getName());
            s.append("      <item");
            s.append(" name=\"").append(fieldName).append("\"");
            if (c.isDate()) {
                s.append(" type=\"").append("Datetime").append("\"");
                s.append(" format=\"").append("yyyy-MM-dd HH:mm:ss").append("\"");
            } else if (c.isDouble()) {
                s.append(" type=\"").append("Decimal").append("\"");
                s.append(" format=\"").append("0.00").append("\"");
            } else if (c.isNum()) {
                s.append(" type=\"").append("INT").append("\"");
            } else {
                s.append(" type=\"").append("String").append("\"");
            }
            s.append(" />").append("\r\n");
            if (c.isDate()) {
                s.append("      <item");
                s.append(" name=\"").append(fieldName).append("Begin\"");
                s.append(" type=\"").append("Datetime").append("\"");
                s.append(" format=\"").append("yyyy-MM-dd HH:mm:ss").append("\"");
                s.append(" />").append("\r\n");
                s.append("      <item");
                s.append(" name=\"").append(fieldName).append("End\"");
                s.append(" type=\"").append("Datetime").append("\"");
                s.append(" format=\"").append("yyyy-MM-dd HH:mm:ss").append("\"");
                s.append(" />").append("\r\n");

            }
        }
        s.append("  </model>").append("\r\n");
        return s.toString();
    }

    private static String fromWhereSql(List<Column> list, String table) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <sql id=\"queryFromWhereSql\">").append("\r\n");
        s.append("    FROM " + table + " t").append("\r\n");
        s.append("    <where>").append("\r\n");
        String name;
        String fieldName;
        Column c = null;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            name = c.getName().toUpperCase();
            fieldName = NameUtil.fieldName(c.getName());
            s.append("    <if test=\"" + fieldName + " != null\"> ").append("\r\n");
            s.append("      and t." + name + " = #{" + fieldName + "}").append("\r\n");
            s.append("    </if> ").append("\r\n");
            if (c.isDate()) {
                s.append("    <if test=\"" + fieldName + "Begin != null\"> ").append("\r\n");
                s.append("      and t." + name + " &gt;= #{" + fieldName + "Begin}").append("\r\n");
                s.append("    </if> ").append("\r\n");
                s.append("    <if test=\"" + fieldName + "End != null\"> ").append("\r\n");
                s.append("      and t." + name + " &gt;= #{" + fieldName + "End}").append("\r\n");
                s.append("    </if> ").append("\r\n");
            }
        }
        s.append("    </where>").append("\r\n");
        s.append("  </sql>").append("\r\n");
        return s.toString();
    }

    private static String querySql(List<Column> list) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <select id=\"query\" parameterType=\"Map\" resultType=\"Map\">").append("\r\n");
        s.append("    SELECT ").append("\r\n");
        String name;
        String fieldName;
        Column c;
        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            name = c.getName().toUpperCase();
            fieldName = NameUtil.fieldName(c.getName());
            s.append("    t.").append(name).append(" ").append(fieldName);
            if (i < list.size() - 1) {
                s.append(",");
            }
            s.append("\r\n");
        }
        s.append("    <include refid=\"queryFromWhereSql\"/>").append("\r\n");
        s.append("    <if test=\"orderBy != null\">").append("\r\n");
        s.append("      order by ${orderBy}").append("\r\n");
        s.append("    </if>").append("\r\n");
        s.append("  </select>").append("\r\n");
        return s.toString();
    }

    private static String countSql() throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("  <select id=\"queryCount\" parameterType=\"Map\" resultType=\"java.lang.Long\">").append("\r\n");
        s.append("    SELECT count(t.ID) ").append("\r\n");
        s.append("    <include refid=\"queryFromWhereSql\"/>").append("\r\n");
        s.append("  </select>").append("\r\n");
        return s.toString();
    }

}
