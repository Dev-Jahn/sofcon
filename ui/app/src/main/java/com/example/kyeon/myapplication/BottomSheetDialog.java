package com.example.kyeon.myapplication;

import android.content.res.Resources;
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
    final int ITEM_SIZE = 8;
    public static BottomSheetDialog getInstance() { return new BottomSheetDialog();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        recyclerView = view.findViewById(R.id.rec_view_recommend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /**
         * refactored by archslaveCW
         * reference : res/values/dialog_strings.xml
         */
        Resources resources = getResources();

        List<Rec_Item> rec_items = new ArrayList<>();
        Rec_Item[] rec_item = new Rec_Item[ITEM_SIZE];
        rec_item[0] = new Rec_Item(R.drawable.recommend_client, resources.getString(R.string.customized_tour), resources.getString(R.string.customized_explain), "#1565C0");
        rec_item[1] = new Rec_Item(R.drawable.recommend_activity, resources.getString(R.string.outdoor_tour), resources.getString(R.string.outdoor_explain), "#00695C");
        rec_item[2] = new Rec_Item(R.drawable.recommend_rest, resources.getString(R.string.healing_tour), resources.getString(R.string.healing_explain), "#8BC34A");
        rec_item[3] = new Rec_Item(R.drawable.recommend_food, resources.getString(R.string.gourmet_tour),resources.getString(R.string.gourmet_explain), "#F44336");
        rec_item[4] = new Rec_Item(R.drawable.recommend_night, resources.getString(R.string.night_tour), resources.getString(R.string.night_explain), "#AA00FF");
        rec_item[5] = new Rec_Item(R.drawable.recommend_monument, resources.getString(R.string.landmark_tour), resources.getString(R.string.landmark_explain),"#F4511E");
        rec_item[6] = new Rec_Item(R.drawable.recommend_camera, resources.getString(R.string.picture_tour), resources.getString(R.string.picture_explain),"#37474F");
        rec_item[7] = new Rec_Item(R.drawable.recommend_shopping, resources.getString(R.string.shopping_tour), resources.getString(R.string.shopping_explain),"#FF6D00");


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
