package com.lxj.xpopupextdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lxj.xpopupext.popup.TimePickerPopup;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        findViewById(R.id.btnTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                date.set(2000, 5, 1);
                Calendar date2 = Calendar.getInstance();
                date2.set(2020, 5, 1);
                TimePickerPopup popup = new TimePickerPopup(MainActivity.this)
//                        .setMode(TimePickerPopup.Mode.YMDHMS)
                        .setDefaultDate(date)  //设置默认选中日期
//                        .setYearRange(1990, 1999) //设置年份范围
                        .setDateRange(date, date2) //设置日期范围
                        .setCyclic(false) //设置不循环
                        .setTimePickerListener(new TimePickerListener() {
                            @Override
                            public void onTimeChanged(Date date) {
                                //时间改变
                            }

                            @Override
                            public void onTimeConfirm(Date date, View view) {
                                //点击确认时间
                                Toast.makeText(MainActivity.this, "选择的时间：" + date.toLocaleString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                new XPopup.Builder(MainActivity.this)
                        .borderRadius(30)
//                        .isDarkTheme(true)
                        .asCustom(popup)
                        .show();
            }
        });
        initJsonData();
        findViewById(R.id.btnCity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPickerPopup<String> popup = new CityPickerPopup<>(MainActivity.this);
                popup.setPicker(options1Items,options2Items,options3Items);
                popup.setCityPickerListener(new CityPickerListener() {
                    @Override
                    public void onCityConfirm(int provinceIndex, int cityIndex, int areaIndex, View v) {
                        Log.e("tag", provinceIndex + " - " + cityIndex + " - " + areaIndex);
                        Toast.makeText(MainActivity.this, provinceIndex + " - " + cityIndex + " - " + areaIndex, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCityChange(int provinceIndex, int cityIndex, int areaIndex) {
                        Log.e("tag", provinceIndex + " - " + cityIndex + " - " + areaIndex);
                        Toast.makeText(MainActivity.this, provinceIndex + " - " + cityIndex + " - " + areaIndex, Toast.LENGTH_SHORT).show();
                    }
                });
                new XPopup.Builder(MainActivity.this)
                        .borderRadius(30)
//                        .isDarkTheme(true)
                        .asCustom(popup)
                        .show();
            }
        });

        findViewById(R.id.btnCommon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonPickerPopup popup = new CommonPickerPopup(MainActivity.this);
                ArrayList<String> list = new ArrayList<String>();
                list.add("小猫");
                list.add("小狗");
                list.add("小羊");
                popup.setPickerData(list)
                        .setCurrentItem(1);
                popup.setCommonPickerListener(new CommonPickerListener() {
                    @Override
                    public void onItemSelected(int index, String data) {
                        Toast.makeText(MainActivity.this, "选中的是 " + data, Toast.LENGTH_SHORT).show();
                    }
                });
                new XPopup.Builder(MainActivity.this)
                        .borderRadius(30)
                        .isDarkTheme(true)
                        .asCustom(popup)
                        .show();
            }
        });
    }


    private void initJsonData() {//解析数据

        String JsonData = readJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
//        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            options1Items.add(jsonBean.get(i).getName());
            List<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    public String readJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}