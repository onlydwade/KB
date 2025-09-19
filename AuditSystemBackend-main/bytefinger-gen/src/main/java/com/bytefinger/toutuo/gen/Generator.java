package com.bytefinger.toutuo.gen;


/**
 * 自动通过数据库的表和字段生成各层代码的工具
 *
 * @author pat
 * @date 2021/7/26 22:54
 **/
public class Generator {

    public static void main(String[] args) {
        String dataSourceurl = "jdbc:mysql://mysql-abee4b2f7dcd-public.rds.volces.com:13306/bytefinger-audit?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
        String dataSourcename = "db_admin";
        String dataSourcepassword = "1qaz@WSXP@ssw0rd";
        String dataSourcedriver = "com.mysql.jdbc.Driver";
        String[] tables = {"biz_audit_topic"};
        String tablePrefix = "biz_";
        String packageParent = "com.bytefinger.toutuo.biz";
        String moduleName = "audittopic";
        String author = "Jone";

        new MybatiesPlusGenerator().generator(dataSourceurl, dataSourcename, dataSourcepassword, dataSourcedriver, tables, tablePrefix, packageParent, moduleName, author);
    }

}
