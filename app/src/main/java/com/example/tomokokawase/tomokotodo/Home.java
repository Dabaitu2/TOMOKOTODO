package com.example.tomokokawase.tomokotodo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.tomokokawase.tomokotodo.adapters.CalendarFragmentAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    private MyCalendarViewPager myCalendarPager;
    private GridView titleGridView;
    private SimpleAdapter title_adapter;
    private final static String title[] = {"日", "一", "二", "三", "四", "五", "六"};
    private List<Map<String, String>> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initTitle();


        myCalendarPager = findViewById(R.id.viewPager_calendar);
        List<Fragment> calendarList = new ArrayList<>();

        CalendarFragmentAdapter calendarFragmentAdapter = new CalendarFragmentAdapter(getSupportFragmentManager(), calendarList);

        myCalendarPager.setAdapter(calendarFragmentAdapter);
        myCalendarPager.getFragmentAdapter(calendarFragmentAdapter);
        myCalendarPager.setCurrentItem(1);
    }


    private void initTitle() {
        titleGridView = findViewById(R.id.gridView_title);

        titleList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("title", title[i]);
            titleList.add(item);
        }

        String from[] = {"title"};
        int to[] = {R.id.textView_date_title};

        title_adapter = new SimpleAdapter(this, titleList, R.layout.gridview_title_item, from, to);
        titleGridView.setAdapter(title_adapter);
    }
}
