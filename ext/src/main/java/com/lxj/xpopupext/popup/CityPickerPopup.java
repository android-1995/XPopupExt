package com.lxj.xpopupext.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.contrarywind.view.WheelView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupext.R;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.OnOptionsSelectListener;
import com.lxj.xpopupext.view.WheelOptions;

import java.util.List;

public class CityPickerPopup<T> extends BottomPopupView {
    private CityPickerListener cityPickerListener;
    private WheelOptions<T> wheelOptions;
    public int dividerColor = 0xFFd5d5d5; //分割线的颜色
    public float lineSpace = 2.4f; // 条目间距倍数 默认2
    public int textColorOut = 0xFFa8a8a8; //分割线以外的文字颜色
    public int textColorCenter = 0xFF2a2a2a; //分割线之间的文字颜色
    public int option1, option2, option3;//默认选中项
    private List<T> mOptions1Items;
    private List<List<T>> mOptions2Items;
    private List<List<List<T>>> mOptions3Items;
    private boolean isCyclic = false; //是否循环
    private CharSequence submitText; //确定的文案
    private CharSequence cancelText; //取消的文案
    public int textColorCancel = Color.parseColor("#666666"); //取消的文字颜色
    private int itemsVisibleCount = 7;
    private int itemTextSize = 18;

    public CityPickerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_ext_city_picker;
    }

    TextView btnCancel, btnConfirm;

    @Override
    protected void onCreate() {
        super.onCreate();
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        if (submitText != null) {
            btnConfirm.setText(submitText);
        }
        if (cancelText != null) {
            btnCancel.setText(cancelText);
        }
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityPickerListener != null) {
                    int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                    int options1 = optionsCurrentItems[0];
                    int options2 = optionsCurrentItems[1];
                    int options3 = optionsCurrentItems[2];
                    cityPickerListener.onCityConfirm(options1, options2, options3, v);
                }
                dismiss();
            }
        });

        wheelOptions = new WheelOptions<>(findViewById(R.id.citypicker), false);
        if (cityPickerListener != null) {
            wheelOptions.setOptionsSelectChangeListener(new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3) {
                    cityPickerListener.onCityChange(options1, options2, options3);
                }
            });
        }
        wheelOptions.setTextContentSize(itemTextSize);
        wheelOptions.setItemsVisible(itemsVisibleCount);
        wheelOptions.setAlphaGradient(true);
        wheelOptions.setCyclic(isCyclic);

        wheelOptions.setDividerColor(popupInfo.isDarkTheme ? Color.parseColor("#444444") : dividerColor);
        wheelOptions.setDividerType(WheelView.DividerType.FILL);
        wheelOptions.setLineSpacingMultiplier(lineSpace);
        wheelOptions.setTextColorOut(textColorOut);
        wheelOptions.setTextColorCenter(popupInfo.isDarkTheme ? Color.parseColor("#CCCCCC") : textColorCenter);
        wheelOptions.isCenterLabel(false);

        wheelOptions.setPicker(mOptions1Items, mOptions2Items, mOptions3Items);
        reSetCurrentItems();

        if (popupInfo.isDarkTheme) {
            applyDarkTheme();
        } else {
            applyLightTheme();
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        btnCancel.setTextColor(textColorCancel);
        btnConfirm.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        getPopupImplView().setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_dark_color),
                popupInfo.borderRadius, popupInfo.borderRadius, 0, 0));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        btnCancel.setTextColor(textColorCancel);
        btnConfirm.setTextColor(XPopup.getPrimaryColor());
        getPopupImplView().setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_light_color),
                popupInfo.borderRadius, popupInfo.borderRadius, 0, 0));
    }

    private void reSetCurrentItems() {
        if (wheelOptions != null) {
            wheelOptions.setCurrentItems(option1, option2, option3);
        }
    }

    public CityPickerPopup<T> setCityPickerListener(CityPickerListener listener) {
        this.cityPickerListener = listener;
        return this;
    }

    /**
     * 设置默认选中项
     *
     * @param option1
     */
    public CityPickerPopup<T> setSelectOptions(int option1) {
        this.option1 = option1;
        return this;
    }


    public CityPickerPopup<T> setSelectOptions(int option1, int option2) {
        this.option1 = option1;
        this.option2 = option2;
        return this;
    }

    public CityPickerPopup<T> setSelectOptions(int option1, int option2, int option3) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        return this;
    }

    public CityPickerPopup<T> setPicker(List<T> optionsItems) {
        this.setPicker(optionsItems, null, null);
        return this;
    }

    public CityPickerPopup<T> setPicker(List<T> options1Items, List<List<T>> options2Items) {
        this.setPicker(options1Items, options2Items, null);
        return this;
    }

    public CityPickerPopup<T> setPicker(List<T> options1Items,
                                        List<List<T>> options2Items,
                                        List<List<List<T>>> options3Items) {
        mOptions1Items = options1Items;
        mOptions2Items = options2Items;
        mOptions3Items = options3Items;
        return this;
    }

    public CityPickerPopup<T> setCyclic(boolean cyclic) {
        this.isCyclic = cyclic;
        return this;
    }


    public CityPickerPopup<T> setSubmitText(CharSequence submitText) {
        this.submitText = submitText;
        return this;
    }

    public CityPickerPopup<T> setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public CityPickerPopup<T> setItemsVisibleCount(int itemsVisibleCount) {
        this.itemsVisibleCount = itemsVisibleCount;
        return this;
    }

    public CityPickerPopup<T> setItemTextSize(int textSize) {
        this.itemTextSize = textSize;
        return this;
    }

    public CityPickerPopup<T> setLineSpace(float lineSpace) {
        this.lineSpace = lineSpace;
        return this;
    }

    /**
     * 分割线颜色设置
     */
    public CityPickerPopup<T> setDividerColor(@ColorInt int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    /**
     * 未选中项文字颜色设置
     */
    public CityPickerPopup<T> setTextColorOut(@ColorInt int textColorOut) {
        this.textColorOut = textColorOut;
        return this;
    }

    /**
     * 选中项文字颜色设置
     */
    public CityPickerPopup<T> setTextColorCenter(@ColorInt int textColorCenter) {
        this.textColorCenter = textColorCenter;
        return this;
    }


    /**
     * 取消文字颜色设置
     */
    public CityPickerPopup<T> setCancelTextColor(@ColorInt int textColor) {
        this.textColorCancel = textColor;
        return this;
    }
}
