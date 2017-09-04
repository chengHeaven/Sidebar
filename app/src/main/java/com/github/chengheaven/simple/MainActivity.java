package com.github.chengheaven.simple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.github.chengheaven.sidebar.OnSidebarItemClickListener;
import com.github.chengheaven.sidebar.SidebarView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private SidebarView sidebarView;

    private TextView textView;

    private Button button;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sidebarView = (SidebarView) findViewById(R.id.sidebar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        int[] image = {R.drawable.ic_mic_black_24dp,
                R.drawable.icon,
                R.drawable.menu_icon_help_sel,
                R.drawable.github_circle};

        int[] color = {ContextCompat.getColor(this, R.color.firstColor),
                ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor),
                ContextCompat.getColor(this, R.color.fourthColor)};

        int[] normalDrawableIds = {R.drawable.menu_icon_search_nor,
                R.drawable.menu_icon_me_nor,
                R.drawable.menu_icon_offline_nor,
                R.drawable.menu_icon_help_nor,
                R.drawable.menu_icon_set_nor
        };

        int[] selectDrawableIds = {R.drawable.menu_icon_search_sel,
                R.drawable.menu_icon_me_sel,
                R.drawable.menu_icon_offline_sel,
                R.drawable.menu_icon_help_sel,
                R.drawable.menu_icon_set_sel
        };

        if (sidebarView != null) {

            // 是否显示文字
            sidebarView.isShowText(true);

            // 默认选中 index ； 不默认选中 则为 -1; 当 setViewPager 时, mCurrentItem value set 0
            sidebarView.setDefaultSelectItemOfIndex(-1);

            // 点击 item 是否改变 Sidebar 的背景颜色
            sidebarView.isClickChangeBackgroundColor(true);

            // Sidebar 背景颜色, 当 显示动画效果且 isClickChangeBackgroundColor 为true 时, 无效
            sidebarView.setBaseBackgroundColor(getResources().getColor(R.color.colorPink));

            // 选中 item 的颜色
            sidebarView.setItemSelectedColor(getResources().getColor(R.color.colorWhite));

            // 未选中 Item 的颜色
            sidebarView.setItemUnSelectedColor(getResources().getColor(R.color.colorGray));

            sidebarView.setItemSelectedTextColor(getResources().getColor(R.color.itemClickColor));
            sidebarView.setItemUnSelectedTextColor(getResources().getColor(R.color.colorCC));

            // Sidebar 方向
//            sidebarView.orientationIsVertical();

            // Sidebar 横向的高度 或者 竖向 item 的宽高
            sidebarView.setSidebarWidth(100);

            // 点击 item 是否有动画效果
            sidebarView.isAnimator(true);

            // 点击事件响应已选中 Item
            sidebarView.isSelectedItemClick(true);

            // if isAnimator is true, 点击 Item, Item 偏移量
            sidebarView.setAnimatorPadding(30);

            //if isAnimator is true, Item 初始偏移量
            sidebarView.setInitializePadding(10);

            // 隐藏 Item， 传值类型为 int[] 数组
//            sidebarView.setItemGone(new int[]{1});
        }

        /*
         * 给 Sidebar 添加按钮
         */

//        SidebarBean sidebarBean = new SidebarBean("Record", image[0]);
//        SidebarBean sidebarBean1 = new SidebarBean("Like", image[1]);
//        SidebarBean sidebarBean2 = new SidebarBean("Books", image[2]);
//        SidebarBean sidebarBean3 = new SidebarBean("GitHub", image[3]);

//        SidebarBean sidebarBean = new SidebarBean("Record", 0, normalDrawableIds[0], selectDrawableIds[0]);
//        SidebarBean sidebarBean1 = new SidebarBean("Like", 0, normalDrawableIds[1], selectDrawableIds[1]);
//        SidebarBean sidebarBean2 = new SidebarBean("Books", 0, normalDrawableIds[2], selectDrawableIds[2]);
//        SidebarBean sidebarBean3 = new SidebarBean("GitHub", 0, normalDrawableIds[3], selectDrawableIds[3]);


//        sidebarView.addTab(sidebarBean);
//        sidebarView.addTab(sidebarBean1);
//        sidebarView.addTab(sidebarBean2);
//        sidebarView.addTab(sidebarBean3);


        // 给 Sidebar 添加一个 ViewPager
        List<Fragment> fragments = new ArrayList<>();

        FirstFragment first = FirstFragment.newInstance();
        FirstFragment second = FirstFragment.newInstance();
        FirstFragment three = FirstFragment.newInstance();
        FirstFragment four = FirstFragment.newInstance();

        fragments.add(first);
        fragments.add(second);
        fragments.add(three);
        fragments.add(four);

        List<String> titles = new ArrayList<>();
        titles.add("sidebar 1");
        titles.add("sidebar 2");
        titles.add("sidebar 3");
        titles.add("sidebar 4");


        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles));

        viewPager.addOnPageChangeListener(this);
        sidebarView.setUpWithViewPager(viewPager, color, image);


        sidebarView.setOnSidebarClickListener(new OnSidebarItemClickListener() {

            int i = 0;

            @Override
            public void onSidebarItemClick(int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        i++;
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        sidebarView.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
