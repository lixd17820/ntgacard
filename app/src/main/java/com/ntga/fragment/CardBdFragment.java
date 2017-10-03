package com.ntga.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntga.card.App;
import com.ntga.card.BdInfo;
import com.ntga.card.MainActivity;
import com.ntga.card.R;
import com.ntga.card.SdryInfo;
import com.ntga.card.SdryInfo_;
import com.ntga.card.ZtryInfo;
import com.ntga.card.ZtryInfo_;
import com.ntga.web.BdEvent;
import com.ntga.web.NetwokThread;
import com.ntga.web.NetwrokEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.objectbox.Box;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardBdFragment extends Fragment {

    private TextView et_name, et_nation, et_bir, et_address, et_id2Num,
            et_issnue, et_time, et_sex, mTVHint;
    private ImageView mIVHead;

    private long time;

    private Box<ZtryInfo> ztry;
    private Box<BdInfo> bdBox;
    private Box<SdryInfo> sdry;

    public CardBdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        App app = (App) getActivity().getApplication();
        ztry = app.getBoxStore().boxFor(ZtryInfo.class);
        bdBox = app.getBoxStore().boxFor(BdInfo.class);
        sdry = app.getBoxStore().boxFor(SdryInfo.class);
        Log.e("CardBdFragment", "CardBdFragment onCreateView");
        View v = inflater.inflate(R.layout.card_bd_fragment, container, false);
        initWidgets(v);
        clearInfo();
        mTVHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - time < 500) {
                    clearInfo();
                } else {
                    time = System.currentTimeMillis();
                }
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("CardBdFragment", "CardBdFragment onStart");

    }

    /**
     * @throws
     * @Title: initWidgets
     * @author xc
     * @Description: TODO(初始化)
     * @param:
     * @return: void
     */
    private void initWidgets(View view) {
        et_name = (TextView) view.findViewById(R.id.et_XM);
        et_sex = (TextView) view.findViewById(R.id.et_sex);
        et_nation = (TextView) view.findViewById(R.id.et_MZ);
        et_bir = (TextView) view.findViewById(R.id.et_CSRQ);
        et_address = (TextView) view.findViewById(R.id.et_HJDXZ);
        et_id2Num = (TextView) view.findViewById(R.id.et_GMSFHM);
        et_issnue = (TextView) view.findViewById(R.id.et_issnue);
        et_time = (TextView) view.findViewById(R.id.et_time);
        mIVHead = (ImageView) view.findViewById(R.id.iv_idCardFace);
        mTVHint = (TextView) view.findViewById(R.id.mTVHint);

    }

    public void clearInfo() {
        this.et_name.setText(""); // 姓名
        this.et_sex.setText(""); // 性别
        this.et_nation.setText(""); // 民族
        // this.mTVBirthDay.setText(""); // 出生日期
        this.et_bir.setText("");
        this.et_address.setText(""); // 住址
        this.et_id2Num.setText(""); // 公民身份号码
        this.et_issnue.setText(""); // 签发机关
        this.et_time.setText(""); // 有效期限
        this.mIVHead.setImageResource(R.drawable.idblank);
        this.mTVHint.setText("");
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * @throws
     * @Title: clearID2Info
     * @author xc
     * @Description: TODO(清除二代证信息)
     * @param:
     * @return: void
     */

    public void showID2Info(String info) {
        try {
            JSONObject obj = new JSONObject(info);
            this.et_name.setText(obj.optString("xm")); // 姓名
            et_sex.setText(obj.optString("sex"));
            this.et_nation.setText(obj.optString("nation")); // 民族
            et_bir.setText(obj.optString("bir"));
            this.et_address.setText(obj.optString("address")); // 住址
            this.et_id2Num.setText(obj.optString("id2Num")); // 公民身份号码
            this.et_issnue.setText(obj.optString("issnue")); // 签发机关
            this.et_time.setText(obj.optString("time")); // 有效期限
            Bitmap bm = BitmapFactory.decodeByteArray(Base64.decode(obj.optString("pic"),
                    Base64.DEFAULT), 0, 38862);
            mIVHead.setImageBitmap(bm);
            BdInfo bd = new BdInfo();
            bd.setXm(obj.optString("xm"));
            String sfzh = obj.optString("id2Num");
            bd.setSfzh(sfzh);
            bd.setBdsj(sdf.format(new Date()));
            if (MainActivity.isOnline) {
                new NetwokThread(2, 1, new Object[]{sfzh, bd}).start();
            }else {
                String bdjg = "";
                ZtryInfo zt = ztry.query().equal(ZtryInfo_.sfzh, sfzh).build().findFirst();
                if(zt != null){
                    bdjg += "有在逃比中信息;\n";
                }
                SdryInfo sd = sdry.query().equal(SdryInfo_.sfzh,sfzh).build().findFirst();
                if(sd != null){
                    bdjg += "有涉毒比中信息;\n";
                }
                if(TextUtils.isEmpty(bdjg)){
                    mTVHint.setText("比对结果：无比中信息");
                }else{
                    bd.setBdjg(bdjg.trim().replaceAll("\n", ""));
                    mTVHint.setText("比对结果：" + bdjg);
                }
            }
            bdBox.put(bd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNetworkStatus(BdEvent e) {
        if (TextUtils.equals("1", e.key)) {
            BdInfo bd = e.id;
            String bdjg = e.value;
            bd.setBdfs(1);
            bd.setBdjg(bdjg.trim().replaceAll("\n", ""));
            bdBox.put(bd);
            mTVHint.setText("比对结果：" + bdjg);
            MainActivity.SectionsPagerAdapter adapter = ((MainActivity) getActivity()).pagerAdapter;
            CountFragment count = (CountFragment) adapter.getRegisteredFragment(1);
            if (count != null)
                count.referList();
        } else {
            mTVHint.setText("比对结果：无比中信息");
        }
    }


}
