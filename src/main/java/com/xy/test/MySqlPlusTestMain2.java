package com.xy.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySqlPlusTestMain2 {

    public static void main(String[] args) {
//		String str = "";
//		str += "< insert >                \n";
//		str += "  < /insert>                 \n";
//		str += "< Insert >               \n";
//		str += "       Hello World!    \n";
//		str += "    < / iNsert >              \n";
//		str += "  <INSERt>                \n";
//		str += "</iNsert>";
//
//		long start = System.currentTimeMillis();
//		try {
//			String sql = new String(Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Desktop\\test", "FormitemMap.xml")));
//			System.out.println(MySqlFormat.format(MySqlPlusKit.compile(sql)));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("耗时：" + (System.currentTimeMillis() - start));

        String temp = "xxx\"123123\" sadfasdf sss=\"5555555555\"";


        Pattern p = Pattern.compile("\"[^\"]*\"");
        String key;
        int len;
        while (true) {

            Matcher matcher = p.matcher(temp);
            if (matcher.find()) {

                key = matcher.group(0);
                len = key.length();
                StringBuilder tempKey = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    tempKey.append("@");
                }
                temp = matcher.replaceFirst(tempKey.toString());
                continue;
            }
             break;

        }
        System.out.println(temp);
    }

}
