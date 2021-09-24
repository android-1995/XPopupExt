package com.lxj.xpopupext.listener;

import android.view.View;

/**
 * Created by xiaosong on 2018/3/20.
 */

public interface CityPickerListener {

    void onCityConfirm(int provinceIndex, int cityIndex, int areaIndex, View v);

    void onCityChange(int provinceIndex, int cityIndex, int areaIndex);

}
