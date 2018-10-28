package com.example.tomokokawase.tomokotodo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.tomokokawase.tomokotodo.adapters.CalendarAdapter;
import com.example.tomokokawase.tomokotodo.utils.MyCalendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class GridViewActivity extends Fragment {

    private GridView gridView;
    private CalendarAdapter adapter;
    private List<MyCalendar.MyDate> myDateList = new ArrayList<>();
    private String year;
    private int month;



    /**
     * 一个用于返回碎片实例的工厂方法，主要是方便包装传值
     * */
    public static final GridViewActivity getInstance(String year, int month){
        GridViewActivity f = new GridViewActivity();
        Bundle bdl = new Bundle();
        bdl.putString("year", year);
        bdl.putInt("month", month);
        f.setArguments(bdl);
        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_grid_view, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            String year = getArguments().getString("year");
            int month = getArguments().getInt("month");
            setMonth(month);
            setYear(year);

            gridView = getView().findViewById(R.id.gridView);



            try {
                adapter = new CalendarAdapter(year, month, getActivity());
                myDateList = adapter.getMyMonthDates();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            gridView.setAdapter(adapter);
            gridView.setEmptyView(getView().findViewById(R.id.nodata));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    for (int i = 0; i < 42; i++) {
                        if (position == i) {
                            myDateList.get(i).setActivated(true);
                        } else {
                            myDateList.get(i).setActivated(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }
    }
