package com.lm.android.gankapp.component.infiniteindicator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.component.infiniteindicator.slideview.BaseSliderView;
import com.lm.android.gankapp.component.infiniteindicator.slideview.MySliderView;
import com.lm.android.gankapp.component.infiniteindicator.slideview.OnSliderClickListener;

import java.util.HashMap;

// TODO infiniteindicator使用参考示例
public class DefaultCircleIndicatorActivity extends FragmentActivity implements OnSliderClickListener {
    private InfiniteIndicatorLayout mDefaultIndicator;
    private HashMap<String, String> url_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_circle_indicator);

        url_maps = new HashMap<String, String>();
        url_maps.put("Page A", "https://raw.githubusercontent.com/lightSky/InfiniteIndicator/master/res/a.jpg");
        url_maps.put("Page B", "https://raw.githubusercontent.com/lightSky/InfiniteIndicator/master/res/b.jpg");
        url_maps.put("Page C", "https://raw.githubusercontent.com/lightSky/InfiniteIndicator/master/res/c.jpg");
        url_maps.put("Page D", "https://raw.githubusercontent.com/lightSky/InfiniteIndicator/master/res/d.jpg");

        testCircleIndicator();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDefaultIndicator.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDefaultIndicator.startAutoScroll();
    }

    private void testCircleIndicator() {
        mDefaultIndicator = (InfiniteIndicatorLayout) findViewById(R.id.indicator_default_circle);
        for (String name : url_maps.keySet()) {
            MySliderView textSliderView = new MySliderView(this);
            textSliderView
                    .setImageUrl(url_maps.get(name))
                    .setOnSliderClickListener(this);
            mDefaultIndicator.addSlider(textSliderView);
        }
        mDefaultIndicator.setIndicatorPosition(InfiniteIndicatorLayout.IndicatorPosition.Center_Bottom);
        mDefaultIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }
}
