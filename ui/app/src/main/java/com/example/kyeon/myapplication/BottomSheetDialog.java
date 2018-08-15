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
    final int ITEM_SIZE = 6;
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
        rec_item[0] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.customized_tour), resources.getString(R.string.customized_explain));
        //init needed(tag or ...)
        rec_item[1] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.outdoor_tour), resources.getString(R.string.outdoor_explain));
        rec_item[2] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.healing_tour), resources.getString(R.string.healing_explain));
        rec_item[3] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.gourmet_tour),resources.getString(R.string.gourmet_explain));
        rec_item[4] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.night_tour), resources.getString(R.string.night_explain));
        rec_item[5] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.landmark_tour), resources.getString(R.string.landmark_explain));
        rec_item[5] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.picture_tour), resources.getString(R.string.picture_explain));
        rec_item[5] = new Rec_Item(R.drawable.arrow_next, resources.getString(R.string.shopping_tour), resources.getString(R.string.shopping_explain));


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
