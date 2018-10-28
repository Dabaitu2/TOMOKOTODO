package com.example.tomokokawase.tomokotodo.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 一个封装了自制日期格式的工具类
 * @author tomokokawase 2018-10-25
 * */
public class MyCalendar {


    /**
     * 格式化数据的方法
     * */
    public static String formatMonth(int month) {
        return month < 10 ? "0" + month : String.valueOf(month);
    }


    /**
     * 描述某一天的子类
     * */
    public class MyDate {
        private String year;
        private String month;
        private String date;
        private DateToLunar.Lunar lunar;
        private Calendar primeDate = Calendar.getInstance();

        private String formatDate(String date) {
            return Integer.valueOf(date) < 10 ? "0" + Integer.valueOf(date) : date;
        }
        private String formatMonth(String month) {
            return Integer.valueOf(month) < 10 ? "0" + (Integer.valueOf(month)) : month;
        }
        private boolean isActivated = false;



        @Override
        public String toString() {
            return "(国历: " + this.date + " , 农历: " + this.lunar.getLunarDay() + ")" ;
        }

        public MyDate(String year, String month, String date) throws ParseException {
            setDate(date);
            setYear(year);
            setMonth(month);
            this.lunar = DateToLunar.solarToLunar(getYear() + getMonth() + getDate());
            this.primeDate.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(date),0,0,0);
        }

        public String getDate() {
            return date;
        }
        public String getYear() {
            return year;
        }
        public String getMonth() {
            return month;
        }
        public boolean isActivated() {
            return isActivated;
        }
        public Calendar getPrimeDate() {
            return this.primeDate;
        }
        public DateToLunar.Lunar getLunar() {
            return lunar;
        }
        public DateToLunar.Lunar getLunarDate() {
            return lunar;
        }


        public void setDate(String date) {
            this.date = formatDate(date);
        }
        public void setYear(String year) {
            this.year = year;
        }
        public void setMonth(String month) {
            this.month = formatMonth(month);
        }
        public void setLunar(DateToLunar.Lunar lunar) {
            this.lunar = lunar;
        }
        public void setLunarDate(DateToLunar.Lunar lunar) {
            this.lunar = lunar;
        }
        public void setActivated(boolean activated) {
            isActivated = activated;
        }



    }

    /**
     * 描述某一周的子类
     */
    public class MyWeek {

        // 内部变量
        private String year;
        private String Month;
        private String weekOfMonth;
        private String weekOfYear;
        private List<MyDate> week = new ArrayList<>();

        // 格式化日期
        private String formatDate(String date) {
            return Integer.valueOf(date) < 10 ? "0" + Integer.valueOf(date) : date;
        }


        // 构造函数
        public MyWeek(String year, int weekOfYear) throws ParseException {
            int daysPast = (weekOfYear - 1) * 7 + 1;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(year), Calendar.JANUARY, 1);
            calendar.add(Calendar.DATE, daysPast - 1);
            setYear(year);
            setMonth(String.valueOf(calendar.get(Calendar.MONTH)));
            setWeekOfYear(String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
            setWeekOfMonth(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH)));
            int todayDayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, -(todayDayOfWeek - 1));
            for (int i = 0; i < 7; i++) {
                this.week.add(new MyDate(year, String.valueOf(calendar.get(Calendar.MONTH) + 1), String.valueOf(calendar.get(Calendar.DATE))));
                calendar.add(Calendar.DATE, 1);
            }
        }
        public MyWeek(String year, String month, String date) throws ParseException {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(date));
            setWeekOfMonth(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH)));
            setWeekOfYear(String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
            setYear(year);
            setMonth(month);
            int todayDayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, -(todayDayOfWeek - 1));
            for (int i = 0; i < 7; i++) {
                this.week.add(new MyDate(year, String.valueOf(calendar.get(Calendar.MONTH) + 1), String.valueOf(calendar.get(Calendar.DATE))));
                calendar.add(Calendar.DATE, 1);
            }
        }
        public MyWeek(String year, String month, int weekOfMonth) throws ParseException {
            if (weekOfMonth > 6) return;

            int daysPast = (weekOfMonth - 1) * 7 + 1;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(year), Integer.valueOf(month) - 1, 1);
            calendar.add(Calendar.DATE, daysPast - 1);

            setYear(year);
            setMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            setWeekOfYear(String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
            setWeekOfMonth(String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH)));

            int todayDayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, -(todayDayOfWeek - 1));
            for (int i = 0; i < 7; i++) {
                MyDate mydate = new MyDate(year, String.valueOf(calendar.get(Calendar.MONTH) + 1), String.valueOf(calendar.get(Calendar.DATE)));
                this.week.add(mydate);
                calendar.add(Calendar.DATE, 1);
            }
        }


        // get方法
        public String getYear() {
            return year;
        }
        public String getMonth() {
            return Month;
        }
        public List<MyDate> getWeek() {
            return week;
        }
        public String getWeekOfMonth() {
            return weekOfMonth;
        }
        public String getWeekOfYear() {
            return weekOfYear;
        }

        // set方法
        public void setYear(String year) {
            this.year = year;
        }
        public void setMonth(String month) {
            this.Month = Integer.valueOf(month) < 10 ? "0" + (Integer.valueOf(month)) : String.valueOf(Integer.valueOf(month));
        }
        public void setWeek(List<MyDate> week) {
            this.week = week;
        }
        public void setWeekOfMonth(String weekOfMonth) {
            this.weekOfMonth = weekOfMonth;
        }
        public void setWeekOfYear(String weekOfYear) {
            this.weekOfYear = weekOfYear;
        }

        // 重写toString方法
        @Override
        public String toString() {
            StringBuilder rtVal = new StringBuilder();
            for (MyDate date: this.week) {
                rtVal.append(date.toString()).append(",");
            }
            return rtVal.toString();
        }
    }

    /**
     * 描述某一个月的子类
     * */
    public class MyMonth {

        // 内部变量
        private List<MyWeek> weeks = new ArrayList<>();
        private String month;
        private String year;
        private int days;

        // 格式化日期
        private String formatDate(String date) {
            return Integer.valueOf(date) < 10 ? "0" + Integer.valueOf(date) : date;
        }
        private String formatMonth(String month) {
            return Integer.valueOf(month) < 10 ? "0" + (Integer.valueOf(month)) : month;
        }

        // 构造方法
        public MyMonth(List<List<Integer>> monthData, String year, String month) throws ParseException {
            this.weeks = new ArrayList<>();
            int count = 0;
            for (List<Integer> week : monthData) {
                Integer tempDate = week.get(0);
                if(count == 0 && tempDate != 1) {
                    this.weeks.add(new MyWeek(year, formatMonth(String.valueOf(Integer.valueOf(month) - 1)), formatDate(String.valueOf(tempDate))));
                } else if(count == 5 && tempDate < 29) {
                    this.weeks.add(new MyWeek(year,formatMonth(String.valueOf(Integer.valueOf(month) + 1)), formatDate(String.valueOf(tempDate))));
                } else {
                    this.weeks.add(new MyWeek(year, month, formatDate(String.valueOf(tempDate))));
                }
                count ++;
            }
            setMonth(month);
            setYear(month);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1,1);
            setDays(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        public MyMonth(String year, int month) throws ParseException {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(year), month - 1, 1);
            int weeknum = calendar.getMaximum(Calendar.WEEK_OF_MONTH);
            for (int i = 1; i <= weeknum; i++) {
                this.weeks.add(new MyWeek(year, String.valueOf(month), i));
            }
            setMonth(String.valueOf(month));
            setYear(year);
            calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1,1);
            setDays(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        // get方法
        public int getDays() {
            return days;
        }
        public String getMonth() {
            return month;
        }
        public String getYear() {
            return year;
        }
        public List<MyWeek> getWeeks() {
            return weeks;
        }

        // set方法
        public void setDays(int days) {
            this.days = days;
        }
        public void setMonth(String month) {
            this.month = Integer.valueOf(month) < 10 ? "0" + (Integer.valueOf(month)) : String.valueOf(Integer.valueOf(month));
        }
        public void setYear(String year) {
            this.year = year;
        }
        public void setWeeks(List<MyWeek> weeks) {
            this.weeks = weeks;
        }

        // 其他方法
        public void addWeek(MyWeek week) {
            this.weeks.add(week);
        }
        public List<MyDate> getDatesPool() {
            if (this.weeks == null) return null;
            List<MyDate> list = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                list.addAll(this.weeks.get(i).getWeek());
            }
            return list;
        }

        // 重写toString
        @Override
        public String toString() {
            StringBuilder rtVal = new StringBuilder();
            for (MyWeek week: this.weeks) {
                rtVal.append("[").append(week.toString()).append("]\n");
            }
            return rtVal.toString();
        }
    }
}
