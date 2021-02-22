package com.xy.sql.parse;

import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.kit.MySqlStandard;
import com.xy.sql.parse.node.MyMapperNode;
import com.xy.sql.parse.node.MyModelItemNode;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlVO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlPlus {

    //缓存
    private static Map<String, MyMapperNode> mapperCache = new HashMap<>();

    /**
     * 解析
     *
     * @param dir
     * @return
     * @throws Exception
     */
    public static void addDirect(File dir) throws Exception {
        if (!dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    addDirect(file);
                } else {
                    if (file.getName().endsWith(".xml")) {
                        addFile(file);
                    }
                }
            }
        } else {
            if (dir.getName().endsWith(".xml")) {
                addFile(dir);
            }
        }
    }


    /**
     * 解析
     *
     * @param sqlFile
     * @return
     * @throws Exception
     */
    private static void addFile(File sqlFile) throws Exception {
        if (!sqlFile.exists()) {
            return;
        }
        String text = new String(Files.readAllBytes(Paths.get(sqlFile.getAbsolutePath())));
        addSqlTxt(text);
    }

    /**
     * 解析     *
     *
     * @param text
     * @throws Exception
     */
    public static void addSqlTxt(String text) throws Exception {
        //格式化
        String xmlSql = MySqlStandard.stdSqlAction(text);
        //解析
        List<MySqlPlusNode> nodeList = MySqlPlusKit.parse(xmlSql);
        if (nodeList == null || nodeList.size() == 0) {
            return;
        }
        //理论上就一个
        int index = 0;
        MyMapperNode mapper;
        for (MySqlPlusNode node : nodeList) {
            mapper = (MyMapperNode) node;
            String namespace = mapper.getNamespace();
            if (namespace == null || namespace.trim().length() <= 0) {
                throw new Exception("Mapper的namespace不能为空，且必须唯一，否则会覆盖！");
            }
            mapperCache.put(namespace, mapper);
        }
    }

    /**
     * 执行
     *
     * @return
     * @throws Exception
     */
    public static SqlVO buildSql(String sqlId, Map<String, Object> inParamters) throws Exception {
        int endIndex = sqlId.lastIndexOf(".");
        String modelName = sqlId.substring(0, endIndex);
        String id = sqlId.substring(endIndex + 1);
        MyMapperNode mapper = mapperCache.get(modelName);
        if (mapper == null) {
            return null;
        }
        SqlVO vo = MySqlPlusKit.bound(new ContextVO(mapper), id, inParamters);
        return vo;

    }

    /**
     * 转换结果
     *
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> convert(String sqlId, List<Map<String, Object>> oriMap) throws Exception {
        int endIndex = sqlId.lastIndexOf(".");
        String modelName = sqlId.substring(0, endIndex);
        MyMapperNode mapper = mapperCache.get(modelName);
        List<Map<String, Object>> resMap = new ArrayList<>();
        if (mapper == null) {
            return resMap;
        }
        //根据模型定义，格式化返回参数
        Map<String, Map<String, String>> itemMap = mapper.getItemMap();
        for (Map<String, Object> data : oriMap) {
            Map<String, Object> newItem = new HashMap<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                newItem.put(entry.getKey(), entry.getValue());
                if (itemMap.containsKey(entry.getKey())) {
                    newItem.put(entry.getKey(), MySqlPlusKit.formatValue(itemMap.get(entry.getKey()).get(MyModelItemNode.ATT_FORMAT), entry.getValue()));
                }
            }
            resMap.add(newItem);
        }
        return resMap;

    }

}
