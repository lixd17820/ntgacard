package com.ntga.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ntga.card.App;
import com.ntga.card.DataDownload;
import com.ntga.card.DataDownload_;
import com.ntga.card.MainActivity;
import com.ntga.card.R;
import com.ntga.web.NetwokThread;
import com.ntga.web.NetwrokEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    private Button updateZtry, updateSdry, changeNetwork;
    private TextView ztryInfo, sdryInfo, network;
    private MaterialDialog dialog;


    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ConfigFragment", "ConfigFragment onCreateView");
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        View v = inflater.inflate(R.layout.fragment_config, container, false);
        updateZtry = v.findViewById(R.id.btn_update_ztry);
        updateSdry = v.findViewById(R.id.btn_update_sdry);
        changeNetwork = v.findViewById(R.id.btn_change_netwrok);
        ztryInfo = v.findViewById(R.id.ztry_info);
        sdryInfo = v.findViewById(R.id.sdry_info);
        network = v.findViewById(R.id.network);
        network.setText(MainActivity.isOnline ? "在线" : "离线");
        updateSdry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        changeNetwork.setOnClickListener(clickNetWork);
        dialog = new MaterialDialog.Builder(getContext())
                .title("系统提示")
                .content("操作中")
                .progress(true, 0).build();
        referZtryInfo(0);
        referZtryInfo(1);
        return v;
    }

    private void referZtryInfo(int catalog) {
        BoxStore bs = ((App) getActivity().getApplication()).getBoxStore();
        Box<DataDownload> downBox = bs.boxFor(DataDownload.class);
        List<DataDownload> downs = downBox.query().equal(DataDownload_.dataCatalog, catalog).build().find();
        String info = "";
        if (downs != null && !downs.isEmpty()) {
            DataDownload d = downs.get(downs.size() - 1);
            info = "最近下载时间：" + d.getXzsj() + "；下载数据：" + d.getCount() + "条";
        } else {
            info = "无离线数据下载记录";
        }
        if(catalog == 0)
            ztryInfo.setText(info);
        else
            sdryInfo.setText(info);
    }

    private View.OnClickListener clickZtry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener clickSdry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener clickNetWork = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (MainActivity.isOnline) {
                MainActivity.isOnline = false;
                network.setText(MainActivity.isOnline ? "在线" : "离线");
                MainActivity a = (MainActivity) getActivity();
                a.setActivityTitle(2);
            } else {
                dialog.show();
                new NetwokThread(1, 0, null).start();
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNetwork(NetwrokEvent e) {
        if (e.from != 1)
            return;
        dialog.dismiss();
        MainActivity.isOnline = e.isConn;
        network.setText(MainActivity.isOnline ? "在线" : "离线");
        Toast.makeText(getContext(), "得到网络信息返回", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ConfigFragment", "ConfigFragment onStart");
    }


    public void test() {
        Toast.makeText(getContext(), "test info", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        Log.e("ConfigFragment", "ConfigFragment onDestroyView");
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        Log.e("ConfigFragment", "ConfigFragment onDestroy");
        super.onDestroy();
    }

}
