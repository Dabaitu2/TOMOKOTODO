package com.example.tomokokawase.tomokotodo.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomokokawase.tomokotodo.R;


/**
 * Created by tomokokawase on 2018/10/26.
 */

public class CalendarViewPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView tv = new TextView(container.getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        tv.setText("第" + position + "页");

        // 添加到ViewPager容器
        container.addView(tv);

        // 返回填充的View对象
        return tv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
