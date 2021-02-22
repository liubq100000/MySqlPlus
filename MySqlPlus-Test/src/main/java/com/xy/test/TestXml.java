package com.xy.test;

import com.xy.sql.build.MysqlPlusCodeBuilder;
import com.xy.sql.parse.MySqlPlus;
import com.xy.sql.parse.dao.BusiDao;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestXml {

    public static void main(String[] args) {
        try {
            String xml = MysqlPlusCodeBuilder.build(TestConnUtil.getConn(), "workflow_form_formitem");
            System.out.println(xml);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
