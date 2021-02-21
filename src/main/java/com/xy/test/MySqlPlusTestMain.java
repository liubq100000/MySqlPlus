package com.xy.test;

import com.xy.sql.parse.MySqlPlus;
import com.xy.sql.parse.dao.BusiDao;
import com.xy.test.db.ConnUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlPlusTestMain {

    public static void main(String[] args) {
        try {
            MySqlPlus.addDirect(new File("C:\\Users\\Administrator\\Desktop\\test"));
            //System.out.println(MySqlFormat.format(MySqlPlusKit.compile(sql)));

            Map<String, Object> p1 = new HashMap<>();
            p1.put("createTime", "2020-11-21 14:31:30");
            p1.put("id","1613891807037");
            Map<String, Object> p = new HashMap<>();
            p.put("createTime", "2020-11-21 14:31:39");
            p.put("type", "12");
            p.put("id", System.currentTimeMillis()+"");
            p.put("formId", System.currentTimeMillis()+"");
            p.put("formName", "测试");
            p.put("columnName", "test123");
            List<Map<String, Object>> dataList = BusiDao.queryForList(ConnUtil.getConn(),"com.FormitemMap.query", p1);
            List<Map<String, Object>> dataList1 = BusiDao.queryForList(ConnUtil.getConn(),"com.FormitemMap.query", p1);
//            if (dataList != null) {
//                System.out.println("个数：" + dataList.size());
//                for (Map<String, Object> data : dataList) {
//                    int i = 0;
//                    for (Map.Entry<String, Object> entry : data.entrySet()) {
//                        if (i > 0) {
//                            System.out.print(",   ");
//                        }
//                        i++;
//                        System.out.print(entry.getValue());
//                    }
//                    System.out.println();
//                }
//            } else {
//                System.out.println("个数：" + 0);
//            }
//            busiDao.execute("com.FormitemMap.update", p1);
//            System.out.println("插入完成");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
