package com.example.tomokokawase.tomokotodo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tomokokawase.tomokotodo.R;
import com.example.tomokokawase.tomokotodo.utils.CalendarGenerator;
import com.example.tomokokawase.tomokotodo.utils.MyCalendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tomokokawase on 2018/10/25.
 * @author tomokokawase
 * 一个用于装载Calendar类与GridView交互的适配器
 */

public class CalendarAdapter extends BaseAdapter {

    private MyCalendar myCalendar = new MyCalendar();
    private MyCalendar.MyMonth myMonth;
    private List<MyCalendar.MyDate> myMonthDates = new ArrayList<>();
    private Context context;
    private Calendar chooseDate = Calendar.getInstance();

    // 这个决定了要填充多少个Item
    @Override
    public int getCount() {
        return myMonth.getDatesPool().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Calendar calendar = myMonthDates.get(position).getPrimeDate();

        if (view == null) {
            // 填充gridView
            view = View.inflate(context, R.layout.gridview_item, null);
            // 初始化holder
            holder = new ViewHolder();
            holder.setDate((TextView) view.findViewById(R.id.textView16));
            holder.setLunarDate((TextView) view.findViewById(R.id.textView18));
            // 初始化当月第一个第一个为默认选中
            if (Integer.valueOf(myMonthDates.get(position).getDate()) == 1 && myMonthDates.get(position).getMonth().equals(myMonth.getMonth())) {
                myMonthDates.get(position).setActivated(true);
            }
            // 如果不是同一个月的就设置为浅色样式
            if (!myMonth.getMonth().equals(myMonthDates.get(position).getMonth())) {
               handleTextColor(holder, R.color.lightgrey);
            }
            // 为view设置holder的tag
            view.setTag(holder);
        } else {
            // 从缓存中读取tag
            holder=(ViewHolder) view.getTag();
        }

        // 为holder 的日期显示填充文本
        holder.getDate().setText(myMonthDates.get(position).getDate());
        // 如果当前实体中的isActivated属性为true, 即被选中，则为其设置选中样式
        if (myMonthDates.get(position).isActivated()) {
            holder.getDate().setBackgroundResource(R.drawable.shape_circle);
            holder.getDate().setTextColor(this.context.getResources().getColor(R.color.white));
            holder.getDate().setActivated(true);
        } else {
            if (calendar.get(Calendar.DAY_OF_YEAR) == chooseDate.get(Calendar.DAY_OF_YEAR)) {
                holder.getDate().setBackgroundResource(R.drawable.shape_today);
            }
            if (!myMonth.getMonth().equals(myMonthDates.get(position).getMonth())) {
                handleTextColor(holder, R.color.lightgrey);
            } else {
                handleTextColor(holder, R.color.black);
            }
            holder.getDate().setActivated(false);
        }

        // 如果当前holder指向今天，则为其添加一些样式
        if (calendar.get(Calendar.DAY_OF_YEAR) == chooseDate.get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == chooseDate.get(Calendar.YEAR)) {
            holder.getDate().setBackgroundResource(R.drawable.shape_today);
            handleDifferentTextColor(holder, R.color.white, R.color.colorAccent);
            if (myMonthDates.get(position).isActivated()) {
                holder.getDate().setBackgroundResource(R.drawable.shape_select);
                holder.getDate().setTextColor(this.context.getResources().getColor(R.color.white));
            }
        }
        // 为holder 的汉字日期填充文本
        holder.getLunarDate().setText(myMonth.getDatesPool().get(position).getLunarDate().getFest());

        return view;
    }

    private void handleDifferentTextColor(ViewHolder holder, int color01, int color02) {
        holder.getDate().setTextColor(this.context.getResources().getColor(color01));
        holder.getLunarDate().setTextColor(this.context.getResources().getColor(color02));
    }
    private void handleTextColor(ViewHolder holder, int color) {
        holder.getDate().setTextColor(this.context.getResources().getColor(color));
        holder.getLunarDate().setTextColor(this.context.getResources().getColor(color));
    }
    public List<MyCalendar.MyDate> getMyMonthDates() {
        return this.myMonthDates;
    }

    // 用于缓存的ViewHolder
    private class ViewHolder {
        TextView Date;
        TextView lunarDate;
        public TextView getDate() {
            return Date;
        }
        public TextView getLunarDate() {
            return lunarDate;
        }
        public void setDate(TextView date) {
            Date = date;
        }
        public void setLunarDate(TextView lunarDate) {
            this.lunarDate = lunarDate;
        }
    }

    // 构造函数
    public CalendarAdapter(String year, int month, Context context) throws ParseException {
        this.myMonth = myCalendar.new MyMonth(CalendarGenerator.GetDates(Integer.valueOf(year), month), year, MyCalendar.formatMonth(month));
        this.myMonthDates = myMonth.getDatesPool();
        this.context = context;
        // 为了确定今天是哪天，需要初始化一下choose变量，存储今天的日期数据
        this.chooseDate.set(
                this.chooseDate.get(Calendar.YEAR),
                this.chooseDate.get(Calendar.MONTH),
                this.chooseDate.get(Calendar.DATE), 0,0,0);
    }
}
