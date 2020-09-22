package com.xy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author XiangYu
 * @create2020-09-22-15:00
 */
public class InputUtil {
    public static final BufferedReader KEYBOARD_INPUT  =new BufferedReader(new InputStreamReader(System.in));

    /**
     * 获取键盘的输入转换为String输出
     *
     * @param prompt 提示语
     * @return
     */
    public static String getString(String prompt){
        boolean flag = true;
        String input = null;
        while (flag){
            System.out.println(prompt);
            try {
                 input = KEYBOARD_INPUT.readLine();
                if(input == null || "".equals(input)){
                    System.out.println("输入为空");
                }else {
                    flag = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("输入错误");
            }

        }
        return input;
    }




}
