/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * © COPYRIGHT 2021 Corporation CAICT All rights reserved.
 * http://www.caict.ac.cn
 */
package cn.bif.common;

public class ToBaseUnit {
    public static Long ToUGas(String bifAmount) {
        return unitWithDecimals(bifAmount, 8);
    }

    public static String ToGas(String moAmount) {
        return unitWithoutDecimals(moAmount, 8);
    }

    /**
     * @param amountWithDecimals The amount without decimals
     * @param decimals The decimals, that cannot be bigger than 18
     * @return The amount with decimals, which means that amountWithDecimals * 10 ^ decimals, but should be bigger than LONG.MAX_VALUE
     */
    public static String unitWithoutDecimals(String amountWithDecimals, int decimals) {
        if (decimals > 18 || decimals < 0) {
            return null;
        }
        // must be long, and the number is between 1 and 19
        if (!amountWithDecimals.matches("(^0?$)|(^[1-9][0-9]{0,18}?$)")) {
            return null;
        }
        String longMaxValue = String.valueOf(Long.MAX_VALUE);
        if ((amountWithDecimals.compareTo(longMaxValue) > 0 && amountWithDecimals.length() == longMaxValue.length()) ||
                amountWithDecimals.length() > longMaxValue.length()) {
            return null;
        }

        String afterStr = "";
        String beforeStr = "";
        if (amountWithDecimals.length() > decimals) {
            afterStr = "." + amountWithDecimals.substring(amountWithDecimals.length() - decimals);
            beforeStr = amountWithDecimals.substring(0, amountWithDecimals.length() - decimals);
        } else {
            String addZero = "";
            for (int i = 0; i < decimals - amountWithDecimals.length(); i++) {
                addZero += "0";
            }
            afterStr = "0." + addZero + amountWithDecimals;
        }
        String result = delEndsZero(beforeStr + afterStr);
        if (result.endsWith(".")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * @param amountWithoutDecimals The amount without decimals, that must be a number smaller than LONG.MAX_VALUE
     * @param decimals The decimals, that should be bigger than and equal to 0 and should be smaller than 19
     * @return The amount with decimals, which means that amountWithDecimals * 10 ^ decimals
     */
    public static Long unitWithDecimals(String amountWithoutDecimals, int decimals) {
        if (decimals > 18 || decimals < 0) {
            return null;
        }
        // long or float string. if float, the number before point cannot be bigger than 10 and the number after point cannot be bigger then 8
        if (!amountWithoutDecimals.matches("(^0(\\.[0-9]{0," + (decimals - 1) + "}[1-9])?$)|" +
                "(^[1-9][0-9]{0," + (18 - decimals) + "}(\\.[0-9]{0," + (decimals - 1) + "}[1-9])?$)")) {
            return null;
        }
        String longMaxValue = String.valueOf(Long.MAX_VALUE);
        String beforeStr = "";
        String afterStr = "";
        String tempArray[] = amountWithoutDecimals.split("\\.");
        if (tempArray.length > 1) {
            beforeStr = tempArray[0];
            afterStr = tempArray[1];
        } else {
            long temp = (long)Math.pow(10, decimals);
            String zeroNumber = Long.toString(temp);
            String amount = amountWithoutDecimals + zeroNumber.substring(1);
            if ((amount.compareTo(longMaxValue) > 0 && amount.length() == longMaxValue.length()) ||
                    amount.length() > longMaxValue.length()){
                return null;
            }
            return Long.parseLong(amount);
        }
        // 0.020000004
        int endIndex = afterStr.length();
        if (afterStr.length() > decimals) {
            endIndex = decimals;
            afterStr = afterStr.substring(0, endIndex);
        } else {
            String addZero = "";
            for (int i = 0; i < decimals - afterStr.length(); i++) {
                addZero += "0";
            }
            afterStr += addZero;
        }
        String amount = delStartZero(beforeStr + afterStr);
        if ((amount.compareTo(longMaxValue) > 0 && amount.length() == longMaxValue.length()) ||
                amount.length() > longMaxValue.length()){
            return null;
        }
        return Long.parseLong(amount);
    }

    public static void main(String[] args) {
        Long s1 = ToBaseUnit.ToUGas("99999999999");
        System.out.println(s1);
        //String s2 = ToBaseUnit.ToGas("999999999999000000");
        //System.out.println(s2);
    }

    public static String delEndsZero(String src) {
        if (src.endsWith("0")) {
            return delEndsZero(src.substring(0, src.length() - 1));
        } else {
            return src;
        }
    }

    public static String delStartZero(String src) {
        if (src.startsWith("0")) {
            return delStartZero(src.substring(1, src.length()));
        } else {
            return src;
        }
    }
}
