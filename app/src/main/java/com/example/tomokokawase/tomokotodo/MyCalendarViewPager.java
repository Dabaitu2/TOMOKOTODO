package com.example.tomokokawase.tomokotodo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.tomokokawase.tomokotodo.adapters.CalendarFragmentAdapter;


/**
 * 继承ViewPager从而实现滚动监听
 * Created by tomokokawase on 2018/10/27.
 */

public class MyCalendarViewPager extends ViewPager{

    private CalendarFragmentAdapter calendarFragmentAdapter;
    private MyCalendarViewPager myself = this;


    public MyCalendarViewPager(Context context) {
        super(context);
    }
    public MyCalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public void getFragmentAdapter(CalendarFragmentAdapter calendarFragmentAdapter) {
        this.calendarFragmentAdapter = calendarFragmentAdapter;
    }
    private void init() {
        addOnPageChangeListener(onPageChangeListener);
    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // 实现无限滑动的平滑切换
            // 这个生命周期位置最平滑，此外要注意notifyDataSetChanged()方法
            // 要想对FragmentAdapter起作用必须修改getItemPosition方法
            switch (state) {
                // 在滚动完成后
                case ViewPager.SCROLL_STATE_IDLE:
                    int currentItem = myself.getCurrentItem();
                    System.out.println("--currentItem--00--:" + currentItem);
                    if (currentItem == 2) {
                        calendarFragmentAdapter.handleSlide(1);
                        calendarFragmentAdapter.notifyDataSetChanged();
                        myself.setCurrentItem(1, true);
                        currentItem = myself.getCurrentItem();
                        System.out.println("--currentItem--01--:" + currentItem);
                    } else if (currentItem == 0) {
                        calendarFragmentAdapter.handleSlide(0);
                        calendarFragmentAdapter.notifyDataSetChanged();
                        myself.setCurrentItem(1, true);
                    }
                    break;
            }
        }
    };

}
