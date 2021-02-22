package com.xy.test;

import com.xy.sql.parse.MySqlPlus;
import com.xy.sql.parse.dao.BusiDao;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {

    public static void main(String[] args) {
        try {
            MySqlPlus.addDirect(new File("F:\\person\\space\\MySqlPlus\\MySqlPlus-Test\\src\\main\\resources"));
            //System.out.println(MySqlFormat.format(MySqlPlusKit.compile(sql)));

            Map<String, Object> p1 = new HashMap<>();
            p1.put("createTimeBegin", "2020-11-20 14:31:33");
//            p1.put("id","1613891807037");
            p1.put("orderBy", "t.create_time");
            Map<String, Object> p = new HashMap<>();
            p.put("createTime", "2020-11-21 14:31:39");
            p.put("type", "12");
            p.put("formId", System.currentTimeMillis()+"");
            p.put("formName", "测试");
            p.put("columnName", "test123");
            List<Map<String, Object>> dataList = BusiDao.queryForList(TestConnUtil.getConn(),"workflow_form_formitem_mapper.query", p1);
            if (dataList != null) {
                System.out.println("个数：" + dataList.size());
                for (Map<String, Object> data : dataList) {
                    int i = 0;
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        if (i > 0) {
                            System.out.print(",   ");
                        }
                        i++;
                        System.out.print(entry.getValue());
                    }
                    System.out.println();
                }
            } else {
                System.out.println("个数：" + 0);
            }
//            BusiDao.execute(TestConnUtil.getConn(),"workflow_form_formitem_mapper.update", p1);
//            System.out.println("插入完成");
//            BusiDao.execute(TestConnUtil.getConn(),"workflow_form_formitem_mapper.insert", p);
//            System.out.println("插入完成");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
