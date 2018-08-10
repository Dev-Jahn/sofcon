package com.example.kyeon.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialog extends BottomSheetDialogFragment{
    RecyclerView recyclerView;
    final int ITEM_SIZE = 5;
    public static BottomSheetDialog getInstance() { return new BottomSheetDialog();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        recyclerView = view.findViewById(R.id.rec_view_recommend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Rec_Item> rec_items = new ArrayList<>();
        Rec_Item[] rec_item = new Rec_Item[ITEM_SIZE];
        rec_item[0] = new Rec_Item(R.drawable.arrow_next,"아웃도어 라이프","액티비티를 좋아하는 당신을 위해서!");
        rec_item[1] = new Rec_Item(R.drawable.arrow_next,"힐링","여행은 편하게 쉬기위해 떠나는것");
        rec_item[2] = new Rec_Item(R.drawable.arrow_next,"맛집 탐방","여행의 백미는 뭐니뭐니해도 식도락");
        rec_item[3] = new Rec_Item(R.drawable.arrow_next,"야밤 투어","낮 보다는 야경이 더 예쁜 사람들에게");
        rec_item[4] = new Rec_Item(R.drawable.arrow_next,"유명지 관광","여기를 왔으면 이거는 보고 가야지!");

        for (int i = 0; i < ITEM_SIZE; i++) {
            rec_items.add(rec_item[i]);
        }
        for (int i = 0; i < 20; i++) {
            rec_items.add(rec_item[2]);
        }

        recyclerView.setAdapter(new RecommendItemAdapter(getContext(), rec_items, R.layout.bottom_sheet_dialog));

        return view;
    }
}
