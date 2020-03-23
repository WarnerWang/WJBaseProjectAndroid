package com.hx.wjbaseproject.util;

import android.graphics.Color;
import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * h5的rule.js
 * common.js
 */
public class Rule {
    private static String formatNum(String num) {
        String pri_num = "";
        if ("3".equals(num)) {
            pri_num = "胜";
        } else if ("1".equals(num)) {
            pri_num = "平";
        } else if ("0".equals(num)) {
            pri_num = "负";
        }
        return pri_num;
    }

    private static int formatScore(String num1Str, String num2Str) {
        int num1 = Integer.parseInt(num1Str);
        int num2 = Integer.parseInt(num2Str);
        int result = 0;
        if (num1 > num2) {
            result = 3;
        } else if (num1 == num2) {
            result = 1;
        } else if (num1 < num2) {
            result = 0;
        }
        return result;
    }

    public static String formatSchemeResult(int type, String result, boolean showRangqiu) throws Exception {
        String pri_result = "";
        if (type == 1) {   //胜平负
            pri_result = formatNum(result);
        } else if (type == 2) {  //让球胜平负
            String[] arr = result.split("#");
            if (showRangqiu) {
                String str = arr[1];
                pri_result = "让" + str + "球" + formatNum(arr[0]);
            }else {
                pri_result = formatNum(arr[0]);
            }

        } else if (type == 3) {  //半全场
            if (result.contains(",")) {
                String[] arr = result.split(",");
                pri_result = formatNum(arr[0]) + formatNum(arr[1]);
            }else if (result.contains("-")) {
                String[] arr = result.split("-");
                pri_result = formatNum(arr[0]) + formatNum(arr[1]);
            }

        } else if (type == 4) {  //比分
            pri_result = result;
            if ("3".equals(result)) {
                pri_result = "胜其它";
            }else if ("1".equals(result)) {
                pri_result = "平其它";
            }else if ("0".equals(result)) {
                pri_result = "负其它";
            }
        } else if (type == 5) {   //总进球
            pri_result = result + " ";
            if ("7".equals(result)) {
                pri_result = result + "+ ";
            }
        } else if (type == 6) {   // 6大小球盘(亚盘)
            pri_result = result.startsWith("3")? "大球":"小球";
        } else if (type == 7) {   //7让球盘(亚盘)
            pri_result = result.startsWith("3")? "上盘":"下盘";
        } else if (type == 8) {   //8角球盘(亚盘)
            pri_result = "8角球盘";
        }
        return pri_result;
    }

    public static String rangqiuString(int type,String result) throws Exception {
        String pri_result = "";
        if (type == 2) {
            String[] arr = result.split("#");
            String str = arr[1];
            if (!StringUtils.isEmpty(str)) {
                if (!str.contains("-") && !str.contains("+")) {
                    str = "+"+str;
                }
            }
            pri_result = "(" + str + ")";
        }
        return pri_result;
    }

    public static int formatRealResult(int type, String schemeResult, String realResult) {
        int pri_result = 0; //0比赛未开始   1//命中   2//未命中
        if (TextUtils.isEmpty(realResult)) {
            return pri_result;
        } else {
            pri_result = 2;
            realResult = realResult.replaceAll("-", ":");
            String[] arr = realResult.split(",");
            String[] half = (arr[0]).split(":");
            String[] full = (arr[1]).split(":");
            if (type == 1) {
                if ((Integer.parseInt(full[0]) > Integer.parseInt(full[1]) && Integer.parseInt(schemeResult) == 3)
                        || (Integer.parseInt(full[0]) == Integer.parseInt(full[1]) && Integer.parseInt(schemeResult) == 1)
                        || (Integer.parseInt(full[0]) < Integer.parseInt(full[1]) && Integer.parseInt(schemeResult) == 0)) {
                    //胜
                    pri_result = 1;
                }
            } else if (type == 2) {
                schemeResult = (schemeResult.split("#"))[0];
                int result = formatScore(full[0], full[1]);
                if (result == Integer.parseInt(schemeResult)) {
                    pri_result = 1;
                }
            } else if (type == 3) {
                String[] arr2 = schemeResult.split(",");
                String result1 = arr2[0];
                String result2 = arr2[1];
                int real_result1 = formatScore(half[0], half[1]);
                int real_result2 = formatScore(full[0], full[1]);
                if (Integer.parseInt(result1) == real_result1 && Integer.parseInt(result2) == real_result2) {
                    pri_result = 1;
                }
            } else if (type == 4) {
                if (schemeResult.equals(arr[1])) {
                    pri_result = 1;
                }
            } else if (type == 5) {
                int allGoal = Integer.parseInt(full[0]) + Integer.parseInt(full[1]);
                if (allGoal == Integer.parseInt(schemeResult)) {
                    pri_result = 1;
                }
            }
        }
        return pri_result;
    }

    public static int getColorWithDiff(float diff){
        if (diff < 0) {
            return Color.parseColor("#14B44F");
        }else if (diff == 0) {
            return Color.parseColor("#333333");
        }else {
            return Color.parseColor("#FF1919");
        }
    }

    public static String getColorWithDiffStr(float diff){
        if (diff < 0) {
            return "#14B44F";
        }else if (diff == 0) {
            return "#333333";
        }else {
            return "#FF1919";
        }
    }

    public static String getRowStr(float diff){
        if (diff < 0) {
            return "↓";
        }else if (diff == 0) {
            return "";
        }else {
            return "↑";
        }
    }

    public static String getFormatStr(String startStr, String immStr) {
        float startValue = Math.abs(StringUtils.convertToFloat(startStr,0));
        float immValue = Math.abs(StringUtils.convertToFloat(immStr,0));
        return "<font color='"+ getColorWithDiffStr(immValue - startValue) +"'>"+ immStr + getRowStr(immValue - startValue) +"</font>";
    }

    public static String getRowFormatStr(String startStr, String immStr){
        float startValue = Math.abs(StringUtils.convertToFloat(startStr,0));
        float immValue = Math.abs(StringUtils.convertToFloat(immStr,0));
        return "<font color='"+ getColorWithDiffStr(immValue - startValue) +"'>" + getRowStr(immValue - startValue) +"</font>";
    }

    public static String formatLotteryTypeName(int lotteryType, int type){
        String name = "竞彩";
        switch (lotteryType) {
            case 1:{
                if (type >= 8) {
                    name = "亚盘胜平负";
                }else {
                    name = "胜平负";
                }
            }
            break;
            case 2:{
                name = "让球胜平负";
            }
            break;
            case 3:{
                name = "半全场";
            }
            break;
            case 4:{
                name = "比分";
            }
            break;
            case 5:{
                name = "总进球";
            }
            break;
            case 6:{
                name = "亚盘大小球";
            }
            break;
            case 7:{
                name = "亚盘让球";
            }
            break;
            case 8:{
                name = "亚盘角球";
            }
            break;
        }
        return name;
    }

    public static int formatLotteryNameColor(int lotteryType, int type){
        String[] colors = {"#FF966D","#FF7272","#FC87BB", "#6DA1FF", "#72E3FF", "#A16DFF"};
        if (lotteryType < 0) {
            lotteryType = 0;
        }
        int index = lotteryType%colors.length;
        return Color.parseColor(colors[index]);
    }

    public static int getColorWithResult(String result) {
        if ("胜".equals(result) || "赢".equals(result)) {
            return Color.parseColor("#D40202");
        } else if ("平".equals(result) || "走".equals(result)) {
            return Color.parseColor("#0287D4");
        } else {
            return Color.parseColor("#03AC0B");
        }
    }

    /**
     * 获取冷门指数
     * @param score
     * @return
     */
    public static float getValidColdNum(double score){
        float result = 0;
        if (score < 10) {
            result = 0.1f;
        }else if (score >= 10 && score < 20) {
            result = (float) (0.1 + ((score - 10)/(20-10))*(3.3-0.1));
        }else if (score >= 20 && score < 38) {
            result = (float) (3.3 + ((score - 20)/(38-20))*(6.6-3.4));
        }else if (score >= 38 && score < 60) {
            result = (float) (6.6 + ((score - 38)/(60-38))*(9.9-6.7));
        }else {
            result = 10;
        }
        return result;
    }

    public static class Results {

        /**
         * 1限时免费 2查看  3已截止 4特权查看  5付费查看
         */
        public int status;
        public ArrayList<Obj> req = Lists.newArrayList();
        public double balance;
        public double redMoney;
        public double surplus;

        public double allCount;//剩余次数 -1为无限次
    }

    public static class Obj {
        public int payType;
        public double money;
    }

}
