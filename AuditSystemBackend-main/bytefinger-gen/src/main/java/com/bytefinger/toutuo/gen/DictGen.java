package com.bytefinger.toutuo.gen;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author pat
 * @date 2022/10/21 14:23
 **/
@Slf4j
public class DictGen {

    public static void main(String[] args) {

//        String s = "";
//        for (String s1 : s.split("、")) {
//            System.out.println(getFirstSpell(s1));
//        }

        one("", "保证金转化类型", "履约保证金、代理服务费", false);


//        one("业务字段类型线索时间字段商机", "业务字段类型线索时间字段商机", "创建时间、最新跟进时间", false);
//        one("业务字段类型线索时间字段项目", "业务字段类型线索时间字段项目", "创建时间、最新跟进时间、合同签订时间、合同到期时间", false);
//        one("业务字段类型线索时间字段客户", "业务字段类型线索时间字段客户", "创建时间、最新跟进时间", false);


//        one("规则符号", "规则符号", "<、>、=、<=、>=、<=", false);
//        tow("模块类型",
//                "线索、商机、项目、客户",
//                new String[]{
//                        "候选人公示、中标公告、合同公告",
//                        "招标预告",
//                        "招标变更",
//                        "招标公告"
//                });
    }

    static void one(String r, String type, String name, boolean flag) {
        String typePingYin = getPingYin(type);
        String[] split = name.split("、");

        System.out.println(String.format("INSERT INTO `sys_dict_type` VALUES (null, '%s', '%s', '', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '%s列表');", type, typePingYin, type));

        int index = 1;
        for (String s : split) {
            String pingYin = getPingYin(s);
            String sql = "INSERT INTO `sys_dict_data` VALUES (null, %d, '%s', '%s', '%s', '', '', 'Y', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '%s');";

            String format = String.format(sql, index++, s, (flag ? (getFirstSpell(type) + "_") : "") + pingYin, typePingYin, (r + s));
            System.out.println(format);
        }
        System.out.println();
    }

    static void tow(String type, String name, String[] erji) {
        String[] split = name.split("、");

        int j = 0;
        for (String s : split) {
            one(type, s, erji[j++], true);
        }
    }


    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else {
                    output += Character.toString(input[i]);
                }

                if (i < input.length - 1) {
                    output += "_";
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            log.error(e.getMessage());
        }
        return output.toUpperCase();
    }


    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null && temp.length > 0) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage());
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();
    }


}
