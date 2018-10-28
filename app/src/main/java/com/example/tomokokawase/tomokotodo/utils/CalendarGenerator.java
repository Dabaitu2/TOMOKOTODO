package com.example.tomokokawase.tomokotodo.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

/**
 * 用来产生模板所需要日期数据的工具类
 * @author tomokokawase 2018-10-25
 * */

public class CalendarGenerator {

    /**
     * @param year  输入的年份
     * @param month 输入的月份
     * @return {@code List<List<Integer>>} 一个6 * 7的数组，符合日历的排列格式
     * */
    public static List<List<Integer>> GetDates (int year, int month) {
        Calendar thisMonth = Calendar.getInstance();
        Calendar lastMonth = Calendar.getInstance();

        // month 是从0开始的，故需要-1
        // 获得前一个月的Calendar对象
        thisMonth.set(year, month - 1 , 1);
        lastMonth.set(year, month - 2 , 1);

        // 获得上个月和这个月的总天数
        int DayOfThisMonth = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int DayOfLastMonth = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 获得这个月的1号是星期几
        int FirstDayOfWeek = thisMonth.get(Calendar.DAY_OF_WEEK) - 1;

        // 指示当前月的日期已经装填到第几天
        int progress = 1;
        // 指示下个月的日期已经装填到第几天
        int nextProgress = 1;

        // 创建一个 6 * 7 的数组来装日期数据
        List<List<Integer>> DatesPool = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            DatesPool.add(new ArrayList<Integer>(7));
        }

        // 装第一行的数据,倒着装让天数自然后退
        List<Integer> Line01 = DatesPool.get(0);
        Stack<Integer> tempStack = new Stack<>();
        for (int i = FirstDayOfWeek; i > 0; i--) {
            tempStack.push(DayOfLastMonth--);
        }
        for (int i = FirstDayOfWeek; i > 0; i--) {
            Line01.add(tempStack.pop());
        }
        for (int i = 7 - FirstDayOfWeek; i > 0; i--) {
            Line01.add(progress++);
        }
        // 计算剩下的天数要占剩余6行中的几行
        // 由于最后多加了一次，指针已经指到了第一个，得减回来到第一行最后一个
        progress--;
        int turn = (int) Math.ceil(((double) DayOfThisMonth - progress) / 7);
        // 这个时候再把指针progress加回去恢复正常
        progress++;

        // 装中间行的本月数据
        // 装中间行的数据
        for (int i = 1; i <= turn; i++) {
            for (int j = 0; j < 7; j++) {
                List<Integer> Line = DatesPool.get(i);
                if (progress <= DayOfThisMonth) {
                    Line.add(progress++);
                }
            }
        }
        for (int i = 1; i < DayOfThisMonth - progress; i++) {
            if (progress <= DayOfThisMonth) {
                List<Integer> Line = DatesPool.get(turn);
                Line.add(progress++);
            }
        }

        // 装下个月的数据
        List LineRest = DatesPool.get(turn);
        int len = LineRest.size();
        for (int i = 0; i < 7 - len; i++) {
            LineRest.add(nextProgress++);
        }

        // 为最后一行补充数据
        // 如果上一个循环已经是第6行，那么这个循环没有用
        // 如果上一个循环是第5行，那这个循环可以补全第6行
        List LineLast = DatesPool.get(5);
        len = LineLast.size();
        for (int i = 0; i < 7 - len; i++) {
            LineLast.add(nextProgress++);
        }
        return DatesPool;
    }

}
