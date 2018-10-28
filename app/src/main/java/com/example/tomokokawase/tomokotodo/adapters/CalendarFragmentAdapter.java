package com.example.tomokokawase.tomokotodo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.example.tomokokawase.tomokotodo.GridViewActivity;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by tomokokawase on 2018/10/26.
 * 用于将Calendar所在的View放入Fragment的适配器
 */

public class CalendarFragmentAdapter extends FragmentStatePagerAdapter {



    private List<Fragment> mylist = new ArrayList<>();
    private FragmentManager myfragmentManager;
    private HashMap<String, Object> currentMonth = new HashMap<>();
    private Calendar currentMonthCalendar = Calendar.getInstance();

    public List<Fragment> getMylist() {
        return mylist;
    }
    public void setMylist(List<Fragment> mylist) {
        this.mylist = mylist;
    }

    public CalendarFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.myfragmentManager = fm;
        this.mylist.addAll(list);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        for(int i = 0; i < 3; i++) {
            int tmp_month = calendar.get(Calendar.MONTH);
            int tmp_year = calendar.get(Calendar.YEAR);
            GridViewActivity calender = GridViewActivity.getInstance(String.valueOf(tmp_year), tmp_month + 1);
            mylist.add(calender);

            if (i == 1) {
                this.currentMonthCalendar.set(tmp_year, tmp_month, 1);
                this.currentMonth = new HashMap<>();
                currentMonth.put("year", String.valueOf(tmp_year));
                currentMonth.put("month", tmp_month + 1);
            }

            calendar.add(Calendar.MONTH, 1);
        }
    }

    public HashMap<String, Object> getCurrentMonth() {
        return this.currentMonth;
    }

    public void handleSlide(int direction) {
        // 前进
        if(direction == 1) {
            mylist.remove(0);
            this.currentMonthCalendar.add(Calendar.MONTH, 1);
            int tmp_month = this.currentMonthCalendar.get(Calendar.MONTH);
            int tmp_year = this.currentMonthCalendar.get(Calendar.YEAR);
            GridViewActivity calender = GridViewActivity.getInstance(String.valueOf(tmp_year), tmp_month + 1);
            mylist.add(calender);
            System.out.println(mylist);

        } else if (direction == 0) { // 后退
            mylist.remove(2);
            this.currentMonthCalendar.add(Calendar.MONTH, -1);
            int tmp_month = this.currentMonthCalendar.get(Calendar.MONTH);
            int tmp_year = this.currentMonthCalendar.get(Calendar.YEAR);
            System.out.println(tmp_month + 1);
            GridViewActivity calender = GridViewActivity.getInstance(String.valueOf(tmp_year), tmp_month + 1);
            mylist.add(0, calender);
            System.out.println(mylist);
        }
    }
    public Calendar getCurrentMonthCalendar() {
        return this.currentMonthCalendar;
    }
    public void upDateFragments() {
        if (this.mylist != null) {
            FragmentTransaction ft = myfragmentManager.beginTransaction();
            for(Fragment f : this.mylist){
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            myfragmentManager.executePendingTransactions();
        }
        notifyDataSetChanged();
    }

    public void setCurrentMonth(String year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(year), month-1, 1);
        calendar.add(Calendar.MONTH, -1);


        for(int i = 0; i < 3; i++) {
            int tmp_month = calendar.get(Calendar.MONTH);
            int tmp_year = calendar.get(Calendar.YEAR);
            GridViewActivity calender = GridViewActivity.getInstance(String.valueOf(tmp_year), tmp_month + 1);
            mylist.add(calender);

            if (i == 1) {
                this.currentMonth = new HashMap<>();
                this.currentMonthCalendar = Calendar.getInstance();
                this.currentMonthCalendar.set(tmp_year, tmp_month);
                currentMonth.put("year", String.valueOf(tmp_year));
                currentMonth.put("month", tmp_month + 1);
            }

            calendar.add(Calendar.MONTH, 1);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public int getCount() {
        return mylist.size();
    }
}
