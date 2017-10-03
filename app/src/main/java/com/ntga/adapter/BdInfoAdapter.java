package com.ntga.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntga.card.BdInfo;
import com.ntga.card.R;

import java.util.List;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class BdInfoAdapter extends RecyclerView.Adapter<BdInfoAdapter.InfoViewHolder> {

    private List<BdInfo> infos;
    private ClickListener ckl;

    public BdInfoAdapter(List<BdInfo> list, ClickListener _ckl) {
        this.infos = list;
        this.ckl = _ckl;
    }

    public interface ClickListener {
        void onClick(int position);
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_captioned_image, parent, false);
        return new InfoViewHolder(view, ckl);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        BdInfo info = infos.get(position);
        holder.text1.setText("姓名：" + info.getXm() + "  " + info.getSfzh());
        String bdjg = TextUtils.isEmpty(info.getBdjg()) ? "无比中信息" : info.getBdjg();
        holder.text2.setText("比对结果：" + bdjg + " " + (info.getBdfs() == 0 ? "离线" : "在线"));
        holder.text3.setText(info.getBdsj().substring(2));
    }

    @Override
    public int getItemCount() {
        return infos == null ? 0 : infos.size();
    }

    public void setList(@NonNull List<BdInfo> _list) {
        infos = _list;
        notifyDataSetChanged();
    }

    static class InfoViewHolder extends RecyclerView.ViewHolder {
        public TextView text1, text2, text3;

        public InfoViewHolder(View itemView, final ClickListener clickListener) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
