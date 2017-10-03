package com.ntga.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ntga.adapter.BdInfoAdapter;
import com.ntga.card.App;
import com.ntga.card.BdInfo;
import com.ntga.card.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    private List<BdInfo> infos;

    private BdInfoAdapter adapter;


    public CountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("CountFragment" , "CountFragment onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        getList();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.gridView1);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BdInfoAdapter(infos, null);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getList() {
        App app = (App) getActivity().getApplication();
        Box<BdInfo> box = app.getBoxStore().boxFor(BdInfo.class);
        long count = box.count();
        long offset = 0, limit = count;
        if (count > 100) {
            offset = count - 100;
            limit = count - offset;
        }
        infos = box.query().build().find(offset, limit);
        if (infos.isEmpty())
            infos = new ArrayList<>();
        Collections.reverse(infos);
    }

    public void referList() {
        getList();
        //Toast.makeText(getContext(), infos.size() + "", Toast.LENGTH_SHORT).show();
        adapter.setList(infos);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("CountFragment" , "CountFragment onStart");

    }

}
