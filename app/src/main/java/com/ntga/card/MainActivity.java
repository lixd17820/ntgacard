package com.ntga.card;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ntga.fragment.CardBdFragment;
import com.ntga.fragment.ConfigFragment;
import com.ntga.fragment.CountFragment;
import com.ntga.web.NetwokThread;
import com.ntga.web.NetwrokEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private BottomNavigationView navigation;
    private CardInfoReciver mCardInfoReciver;
    public static boolean isOnline = false;
    public SectionsPagerAdapter pagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    pager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    pager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    pager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        pagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        pager.addOnPageChangeListener(pageChangeListener);
        boolean isStart = false;
        // 运行服务
        if (!checkServerRunning(this, "com.example.readid2test",
                "com.example.readid2test.ReadCardService")) {
            Intent intent = new Intent();
            intent.setAction("com.ntgajwt.card.info");
            intent.setPackage("com.example.readid2test");
            ComponentName b = startService(intent);
        } else {
            isStart = true;
        }
        Toast.makeText(this, isStart ? "已打开" : "未打开", Toast.LENGTH_LONG).show();

        mCardInfoReciver = new CardInfoReciver();
        IntentFilter filterID2 = new IntentFilter();// 创建IntentFilter对象
        // 注册一个广播，用于接收Activity传送过来的命令，控制Service的行为，如：发送数据，停止服务等
        // filterID2.addAction(ClientVars.receivefromserver);
        filterID2.addAction("com.example.readid2test");
        registerReceiver(mCardInfoReciver, filterID2);
        new NetwokThread(0, 0, null).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNetworkStatus(NetwrokEvent e) {
        Log.e("MainActivity", "getNetworkStatus" + e.from + e.isConn);
        isOnline = e.isConn;
        int ci = pager.getCurrentItem();
        setActivityTitle(ci);
    }

    public void setActivityTitle(int ci) {
        String title = "二代证比对";
        if (ci == 1)
            title = "比对统计";
        else if (ci == 2) {
            title = "系统配置";
        }
        title += (isOnline ? "--在线" : "--离线");
        setTitle(title);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mCardInfoReciver);
        EventBus.getDefault().unregister(this);
        if (checkServerRunning(this, "com.example.readid2test",
                "com.example.readid2test.ReadCardService")) {
            Intent intent = new Intent();
            intent.setAction("com.ntgajwt.card.info");
            intent.setPackage("com.example.readid2test");
            stopService(intent);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CardBdFragment();
                case 1:
                    return new CountFragment();
                case 2:
                    return new ConfigFragment();
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "二代证比对";
                case 1:
                    return "数据统计";
                case 2:
                    return "参数配置";
            }
            return null;
        }
    }

    public static boolean checkServerRunning(Context context, String packName,
                                             String serverName) {
        boolean isRunning = false;
        // 检测服务是否在运行
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo ri : list) {
            if (TextUtils.equals(ri.service.getPackageName(), packName)
                    && TextUtils.equals(ri.service.getClassName(), serverName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public class CardInfoReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (intent.getAction().equals("com.example.readid2test")) {
                String info = bundle.getString("card");
                //showID2Info(info);
                SectionsPagerAdapter adapter = (SectionsPagerAdapter) pager.getAdapter();
                ConfigFragment ft = (ConfigFragment) adapter.getRegisteredFragment(2);
                if (ft != null) {
                    ft.test();
                } else {
                    Toast.makeText(MainActivity.this, "没有初始化", Toast.LENGTH_SHORT).show();
                }
                CardBdFragment fragment = (CardBdFragment) adapter.getRegisteredFragment(0);
                if (fragment != null) {
                    fragment.showID2Info(info);
                    CountFragment cf = (CountFragment) adapter.getRegisteredFragment(1);
                    if (cf != null) {
                        cf.referList();
                    }
                }
            }
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setActivityTitle(position);
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_home);
                    return;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                    return;
                case 2:
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                    return;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
