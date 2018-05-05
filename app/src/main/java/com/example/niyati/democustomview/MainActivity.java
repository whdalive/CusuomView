package com.example.niyati.democustomview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.niyati.democustomview.Pie.PieData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;
    List<PageModel> mPageModels = new ArrayList<>();

    {
        mPageModels.add(new PageModel(R.layout.view_radar,R.string.title_rodar));
        mPageModels.add(new PageModel(R.layout.view_pie,R.string.title_pie));
        mPageModels.add(new PageModel(R.layout.view_watchboard,R.string.title_watchboard));
        mPageModels.add(new PageModel(R.layout.view_circleper,R.string.title_circleper));
        mPageModels.add(new PageModel(R.layout.view_enhancedcircle,R.string.title_enhancedCircle));
        mPageModels.add(new PageModel(R.layout.view_flipboard,R.string.title_flipboard));
        mPageModels.add(new PageModel(R.layout.view_linechart,R.string.title_linechart));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = mPageModels.get(position);
                return PageFragment.newInstance(pageModel.layoutRes);
            }

            @Override
            public int getCount() {
                return mPageModels.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return getString(mPageModels.get(position).titleRes);
            }
        });

        mTabLayout = findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class PageModel{
        @StringRes int titleRes;
        @LayoutRes int layoutRes;

        PageModel( @LayoutRes int layoutRes,@StringRes int titleRes) {
            this.titleRes = titleRes;
            this.layoutRes = layoutRes;
        }
    }
}
