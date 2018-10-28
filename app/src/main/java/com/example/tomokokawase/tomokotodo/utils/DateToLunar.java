package com.example.tomokokawase.tomokotodo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tomokokawase 2018-10-23
 * 将阳历日期转化为阴历日期的工具类
 * 参考学习了 https://blog.csdn.net/Eacter/article/details/42495291
 * 大概方法是计算出阳历日期和1900年1月30的日期差，
 * 然后转化到阴历1900年1月1日开始，加上该时间差并定位到阴历年月
 * */
public class DateToLunar {

    //  1900年1月30日（农历1900年正月初一）开始到2049年12月29日的全部农历数据
    //  农历数据的特征:
    //      1. 5位16进制数，共20bit
    //      2. 最后4bit(16进制末尾1位)标识当前年闰月的月份，为0则该年没有闰月
    //      3. 若2中描述位不为0，则前4bit(16进制0x后第一位)有意义，为1说明当年闰月有30天, 为0则说明29天
    //      4. 中间3个16进制数(12bit描述了每个月的天数，0为29天，1为30天)
    //      5. 以0x04bd8为例, 该年(1900年)的闰月为8月, 闰月有29天
    //         中间3个16进制数(4bd)化为2进制则为 0100 1011 1101(有闰月的情况下中间会插入一个月的闰月)
    //         即1、3、4、6、12月为29天。2、5、7、9、10、13月为30天，8月为闰月,有29天
    private final static int[] LUNAR_INFO = {
            0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2,
            0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977,
            0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970,
            0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950,
            0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557,
            0x06ca0,0x0b550,0x15355,0x04da0,0x0a5d0,0x14573,0x052d0,0x0a9a8,0x0e950,0x06aa0,
            0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0,
            0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b5a0,0x195a6,
            0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570,
            0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x055c0,0x0ab60,0x096d5,0x092e0,
            0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5,
            0x0a950,0x0b4a0,0x0baa4,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930,
            0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530,
            0x05aa0,0x076a3,0x096d0,0x04bd7,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45,
            0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0
    };


    private final static HashMap<String, String> LUNAR_FEST_INFO = new HashMap<String, String>() {
        {
            put("0106", "立春");put("0121", "雨水");put("0206", "惊蛰");put("0221", "春分");
            put("0306", "清明");put("0321", "谷雨");put("0406", "立夏");put("0421", "小满");
            put("0506", "芒种");put("0521", "夏至");put("0606", "小暑");put("0621", "大暑");
            put("0708", "立秋");put("0723", "处暑");put("0801", "白露");put("0824", "秋分");
            put("0829", "寒露");put("0915", "霜降");put("1008", "立冬");put("1023", "小雪");
            put("1108", "大雪");put("1123", "冬至");put("1208", "小寒");put("1223", "大寒");
            put("0909", "重阳");put("0307", "清明");put("0101", "春节");put("0115", "元宵");
            put("0303", "上巳");put("0505", "端午");put("0707", "七夕");put("0815", "中秋");
        }
    };

    private final static String[] DAY_INFO = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    private final static String[] TIANGAN_YEAR_INFO = {"己", "庚", "辛", "壬","癸", "甲", "乙", "丙", "丁", "戊"};
    private final static String[] DIZHI_YEAR_INFO   = {"申", "酉", "戌","亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未"};
    private final static String[] ZODIAC_YEAR_INFO  = {"猴", "鸡", "狗","猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    // 内部类，用来封装阴历数据
    public static class Lunar {
        private String fullTime;
        private String year;
        private String month;
        private String date;
        private String fest;
        private String lunarDay;
        private String lunarYear;
        private String zodiac;

        void setLunarDay() {
            this.lunarDay = "";
            if (getDate() != null) {
                char[] digits = getDate().toCharArray();
                switch (digits[0]) {
                    case '0':
                        this.lunarDay += "初";
                        break;
                    case '1':
                        this.lunarDay += "十";
                        break;
                    case '2':
                        this.lunarDay += "廿";
                        break;
                    case '3':
                        this.lunarDay += "三";
                        break;
                    default:
                        break;
                }
                int digit2 = (int)digits[1] - '0';
                String other = DAY_INFO[((digit2 == 0) ? 10 : digit2) - 1];
                this.lunarDay += other;
                if (this.lunarDay.equals("十十")) {
                    this.lunarDay = "初十";
                }
            }
        }

        public String getLunarDay() {
            return this.lunarDay;
        }

        public String getYear() {
            return year;
        }

        void setYear(String year) {
            this.year = year;
            setLunarYear();
            setZodiac();
        }


        public String getFest() {
            return fest == null ? lunarDay : fest;
        }

        void setFest(String fest) {
            this.fest = LUNAR_FEST_INFO.get(fest);
        }

        public String getFullTime() {

            return fullTime;
        }

        void setFullTime(String fullTime) {
            this.fullTime = fullTime;
        }

        public String getDate() {
            return date;
        }

        void setDate(String date) {
            date = date.length() < 2 ? "0" + date : date;
            this.date = date;
            setLunarDay();
        }

        public String getMonth() {
            return month;
        }

        void setMonth(String month) {
            month = month.length() < 2 ? "0" + month : month;
            this.month = month;
        }

        public String getLunarYear() {
            return lunarYear;
        }

        public void setLunarYear() {
            this.lunarYear = "";
            if (year == null) return;
            String flag_tiangan = year.split("")[3];
            this.lunarYear += TIANGAN_YEAR_INFO[Integer.valueOf(flag_tiangan)];
            int flag_dizhi = Integer.valueOf(year) % 12 ;
            this.lunarYear += DIZHI_YEAR_INFO[(flag_dizhi == 0 ? 12 : flag_dizhi)- 1] + "年";
        }

        public String getZodiac() {
            return zodiac;
        }

        public void setZodiac() {
            this.zodiac = "";
            if (year == null) return;
            int flag_zodiac = Integer.valueOf(year) % 12;
            this.zodiac += ZODIAC_YEAR_INFO[(flag_zodiac == 0 ? 12 : flag_zodiac)- 1] + "年";
        }
    }

    private final static int MAX_YEAR = 2049;
    private final static int MIN_YEAR = 1900;

    // 判断当年是否有闰月的指示变量
    private static boolean isLeapYear;

    // 起始日期和终止日期的限制值
    private final static String START_DATE = "19000130";
    private final static String END_DATE   = "20491229";

    // 判断当前计算状态的常量
    private final static int INVALID_DATA = -1;
    private final static int VALID_DATA   = -2;
    private final static int NO_LEAP      = 0;

    /**
     * 检查输入数据是否合法
     * @param year 输入的年份(阳历年)
     * @return {boolean} 数据是否合法的标识符
     * */
    private static int checkYear(int year) {
        if (year > MAX_YEAR || year < MIN_YEAR) return INVALID_DATA;
        return VALID_DATA;
    }

    /**
     * 返回输入年份的闰月
     * 0xf的高位全为0，和获取到的数据做与运算，求得最后四位的值(1 & 1 = 1, 其余为0)，也就是月份
     * @param year 输入的年份
     * @return {int} 返回的闰月或者是NO_LEAP
     * */
    private static int getLeapMonth(int year) {
        if (checkYear(year) == INVALID_DATA) return INVALID_DATA;
        return LUNAR_INFO[year - 1900] & 0xF;
    }

    /**
     * 返回输入年份的闰月天数
     * 对第前4bit做与运算，获得闰月天数
     * @param year 输入的年份
     * @return {int} 闰月天数或者是NO_LEAP
     * */
    private static int getLeapMonthDays(int year) {
        if (getLeapMonth(year) != NO_LEAP) {
            if((LUNAR_INFO[year - 1900] & 0xF0000)==0){
                return 29;
            }else {
                return 30;
            }
        }
        return getLeapMonth(year);
    }

    /**
     * 返回阴历{@code year}年{@code month}月的天数
     * @param year  阴历年份
     * @param month 阴历月份
     * @return 当月天数或INVALID_DATA
     * */
    private static int getMonthDays(int year, int month) {
        if (month < 1 || month > 12) return INVALID_DATA;

        // 获得要求的那一个月的位置
        // 1的2进制即为 0001，让其按照月份算术左移，
        // 因为20bit中最后四位是指示闰月的，所以要在左移位数上+4,即左移(12 - month) + 4位
        // 最后获得的结果就是只在某一位有1，其余全为0
        int pos = 1 << (16 - month);

        // 然后
        // 1. 获得当前的年份所对应的数据
        // 2. 将当前年份与0x0FFFF做与运算，获得后16位
        // 3. 将后十六位与左移后的数字再与运算，获得那一位的值，
        //    如果那一位原本就是为1，则结果会>0，即30天，否则为29天
        if (((LUNAR_INFO[year - 1900] & 0x0FFFF) & pos) == 0) {
            return 29;
        }else {
            return 30;
        }
    }

    /**
     * 返回阴历{@code year}年的总天数
     * 由于闰月要多一个月，所以最后计算闰月
     * @param year 阴历年份
     * @return 当年天数或INVALID_DATA
     * */
    private static int getYearDays(int year) throws ParseException {
        if (checkYear(year) == INVALID_DATA) return INVALID_DATA;
        int sum = 12 * 29;
        // 让位指针从 1000 0000 0000 0000 开始右移 直到 0000 0000 0000 1000
        // 然后获取该位是否需要加1天的值
        for (int i = 0x8000; i >= 0x8; i >>= 1) {
            if((LUNAR_INFO[year - 1900] & 0xFFF0 & i) != 0) {
                sum++;
            }
        }
        return sum + getLeapMonthDays(year);
    }

    /**
     * 计算两个日期（阳历）相差的天数。
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 两个日期相差的天数
     */
    private static int daysBetween(Date startDate, Date endDate) {

        // 如果startDate晚于endDate就交换位置
        if(startDate.after(endDate)) {
            Date tempDate = new Date(startDate.getTime());
            startDate     = new Date(endDate.getTime());
            endDate       = new Date(tempDate.getTime());
        }

        // 获取Calendar实例
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // 为实例设置时间
        cal1.setTime(startDate);
        cal2.setTime(endDate);

        // 获得年份
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        // 获得本年到设置的时间为止的天数
        int day1  = cal1.get(Calendar.DAY_OF_YEAR);
        int day2  = cal2.get(Calendar.DAY_OF_YEAR);

        // 如果不是同一年
        if(year1 != year2) {
            int yearDistance = 0;
            for(int i = year1 ; i < year2 ; i++)
            {
                // 闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    yearDistance += 366;
                }
                // 平年
                else {
                    yearDistance += 365;
                }
            }
            // 除掉第一年的流逝的时间，加上第二个日期所在年年经过的时间
            return yearDistance + day2 - day1 ;
        } else {
            // 如果是同一年，直接返回相差的天数
            return day2 - day1;
        }
    }

    /**
     * 根据阳历时间推算出阴历时间
     * @param time 满足"yyyyMMdd"的日期格式字符串
     * @return 返回的阴历时间
     * */
    public static <T> T solarToLunar(String time) throws ParseException {

        Lunar lunar = new Lunar();
        Pattern p = Pattern.compile("^\\d{4}\\d{1,2}\\d{1,2}$");
        Matcher m = p.matcher(time);
        if (!m.matches()) return (T)new Integer(INVALID_DATA);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date myDate    = null;
        Date endDate   = null;
        Date startDate = null;

        try {
            startDate = simpleDateFormat.parse(START_DATE);
            endDate   = simpleDateFormat.parse(END_DATE);
            myDate    = simpleDateFormat.parse(time);
            if (myDate.after(endDate) || myDate.before(startDate)) {
                return (T) new Integer(INVALID_DATA);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return (T) new Integer(INVALID_DATA);
        }

        int DaysBetween = daysBetween(startDate, myDate);

        // i用来指示当前的年份
        int i;
        for (i = MIN_YEAR; i <= MAX_YEAR; i++){
            // 求当年农历年天数
            int temp = getYearDays(i);
            // 也就是说到计算了同一年
            if (DaysBetween - temp < 1){
                break;
            }else{
                DaysBetween -= temp;
            }
        }

        // 设置当前的年份
        int lunarYear = i;
        lunar.setYear(String.valueOf(lunarYear));

        //计算该年闰哪个月
        int leapMonth = getLeapMonth(lunarYear);

        //设定当年是否有闰月
        isLeapYear = leapMonth > 0;

        // 开始计算是哪一月哪一日
        int temp = 0, lunarMonth, lunarDay;
        for (i = 1;  i <= 12; i++) {
            // 如果该年是闰年且计算到的该月是闰月就计算闰月天数并减掉
            // 由于遍历过程只有12次，所以闰月的时候先照常计算实际上是闰月前一个月的时间，然后在闰月的后一个月前把闰月挤进去
            // 所以这里要i--
            if (i+1 == leapMonth && isLeapYear) {
                isLeapYear = false;
                temp = getLeapMonthDays(lunarYear);
                i--;
            } else {
                temp = getMonthDays(lunarYear, i);
            }
            // 这个时候的DaysBetween 一定小于355天(一年)
            DaysBetween -= temp;
            if (DaysBetween <= 0) {
                break;
            }
        }

        DaysBetween += temp;
        lunarMonth   = i;
        lunarDay     = DaysBetween;

        lunar.setMonth(String.valueOf(lunarMonth));
        lunar.setDate(String.valueOf(lunarDay));
        lunar.setFullTime(String.valueOf(lunarYear) + "年" + lunar.getMonth() + "月" + lunar.getDate() + "日");
        lunar.setFest(lunar.getMonth() + lunar.getDate());


        return (T) lunar;
    }
}
