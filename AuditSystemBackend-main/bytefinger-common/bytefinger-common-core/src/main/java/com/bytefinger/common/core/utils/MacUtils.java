package com.bytefinger.common.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * MAC地址工具
 *
 * @author patrick
 */
public class MacUtils {

    /**
     * 获取当前操作系统名称.
     *
     * @return 操作系统名称 例如:windows,Linux,Unix等.
     */
    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }


    public static String getMacOSMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            /**
             * Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ifconfig en0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                /**
                 * 寻找标示字符串[hwaddr]
                 */
                index = line.toLowerCase().indexOf("ether");
                /**
                 * 找到了
                 */
                if (index != -1) {
                    /**
                     * 取出mac地址并去除2边空格
                     */
                    mac = line.substring(index + "ether".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }

        return mac;
    }

}
