package com.bytefinger.toutuo.gen;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.web.controller.BaseController;

import java.util.Arrays;
import java.util.List;

/**
 * 自动通过数据库的表和字段生成各层代码的工具
 *
 * @author pat
 * @date 2021/7/26 22:54
 **/
public class MybatiesPlusGenerator {

    public void generator(String dataSourceurl, String dataSourcename, String dataSourcepassword, String dataSourcedriver, String[] tables, String tablePrefix, String packageParent, String moduleName, String author) {
        AutoGenerator mpg = new AutoGenerator();

        // 配置策略
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        // 当前项目的路径
        String projectPath = System.getProperty("user.dir");
        // 生成文件输出根目录
        gc.setOutputDir(projectPath + "/bytefinger-gen/src/main/java");
        // 作者
        gc.setAuthor(author);
        // 生成完成后不弹出文件框
        gc.setOpen(false);
        // 文件是否覆盖
        gc.setFileOverride(true);
        //主键策略 实体类主键ID类型
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setDateType(DateType.ONLY_DATE);
        // 是否开启swagger
        gc.setSwagger2(true);
        //【不懂】 活动记录 不需要ActiveRecord特性的请改为false 是否支持AR模式
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        //【不懂】 XML ResultMap xml映射文件的配置
        gc.setBaseResultMap(true);
        //【不懂】 XML columList xml映射文件的配置
        gc.setBaseColumnList(false);


        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName(dataSourcedriver);
        dsc.setUrl(dataSourceurl);
        dsc.setUsername(dataSourcename);
        dsc.setPassword(dataSourcepassword);
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
        // 可以不用设置，默认是这个
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        // 默认是mapper
        pc.setMapper("mapper");
        // 默认是entity
        pc.setEntity("domain");
        // 默认是默认是mapper.xml
        pc.setXml("mapping");
        // 控制层请求地址的包名显示
        //功能模块名
        pc.setModuleName(moduleName);
        //包名
        pc.setParent(packageParent);
        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动lombok；
        strategy.setEntityLombokModel(true);
        //开启全局大写命名
        strategy.setCapitalMode(false);
        strategy.setSuperMapperClass(null);
        // 控制：true——生成@RsetController false——生成@Controller
        strategy.setRestControllerStyle(true);
        // 表字段注释启动 启动模板中的这个 <#if table.convert>
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 是否删除实体类字段的前缀
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        // 控制层mapping的映射地址 false：infRecData true：inf_rec_data
        strategy.setControllerMappingHyphenStyle(false);
        strategy.setSuperControllerClass(BaseController.class);
//        strategy.setSuperServiceClass(BaseService.class);
//        strategy.setSuperServiceImplClass(BaseServiceImpl.class);
        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");

        // 去掉表名mdm_inf_rec_data中的 mdm_ 类名为InfRecData
        strategy.setTablePrefix(tablePrefix);
        // 逆向工程使用的表   如果要生成多个,这里可以传入String[]
        strategy.setInclude(tables);

        List<TableFill> list = Arrays.asList(
                new TableFill("create_time", FieldFill.INSERT),
                new TableFill("create_user_id", FieldFill.INSERT),
                new TableFill("update_time", FieldFill.INSERT_UPDATE),
                new TableFill("update_user_id", FieldFill.INSERT_UPDATE));
        strategy.setTableFillList(list);

        mpg.setStrategy(strategy);

        //模板生成器
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/template/controller.java");
        tc.setService("/template/service.java");
        tc.setServiceImpl("/template/serviceImpl.java");
        tc.setEntity("/template/entity.java");
        tc.setMapper("/template/mapper.java");
        tc.setXml("/template/mapper.xml");
        mpg.setTemplate(tc);

        //5、执行
        mpg.execute();
    }

}
