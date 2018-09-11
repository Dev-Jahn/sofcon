package com.example.kyeon.myapplication;

import android.content.Intent;
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
        rec_item[0] = new Rec_Item(R.drawable.customized, resources.getString(R.string.customized_tour_landmark), resources.getString(R.string.customized_tour_landmark_explain), resources.getColor(R.color.customized_color));
        rec_item[1] = new Rec_Item(R.drawable.customized, resources.getString(R.string.customized_tour_residence), resources.getString(R.string.customized_tour_residence_explain), resources.getColor(R.color.outdoor_color));
        rec_item[2] = new Rec_Item(R.drawable.customized, resources.getString(R.string.customized_tour_restaurant), resources.getString(R.string.customized_tour_restaurant_explain), resources.getColor(R.color.gourmet_color));
        rec_item[3] = new Rec_Item(R.drawable.healing, resources.getString(R.string.healing_tour), resources.getString(R.string.healing_explain), resources.getColor(R.color.healing_color));
        rec_item[4] = new Rec_Item(R.drawable.landmark, resources.getString(R.string.historic_place), resources.getString(R.string.historic_explain),resources.getColor(R.color.landmark_color));
        rec_item[5] = new Rec_Item(R.drawable.shopping, resources.getString(R.string.shopping_tour), resources.getString(R.string.shopping_explain),resources.getColor(R.color.shopping_color));
        //rec_item[1] = new Rec_Item(R.drawable.outdoor, resources.getString(R.string.outdoor_tour), resources.getString(R.string.outdoor_explain), resources.getColor(R.color.outdoor_color));
        //rec_item[3] = new Rec_Item(R.drawable.gourmet, resources.getString(R.string.gourmet_tour),resources.getString(R.string.gourmet_explain), resources.getColor(R.color.gourmet_color));
        //rec_item[4] = new Rec_Item(R.drawable.night, resources.getString(R.string.night_tour), resources.getString(R.string.night_explain), resources.getColor(R.color.night_color));
        //rec_item[6] = new Rec_Item(R.drawable.picture, resources.getString(R.string.picture_tour), resources.getString(R.string.picture_explain),resources.getColor(R.color.picture_color));


        for (int i = 0; i < ITEM_SIZE; i++) {
            rec_items.add(rec_item[i]);
        }

        Intent intent = new Intent();
        intent.putExtra(MapUtility.PLACE_TYPE_TAG, getArguments().getString(MapUtility.PLACE_TYPE_TAG));
        intent.putExtra(MapUtility.PLACE_LAT_TAG, getArguments().getString(MapUtility.PLACE_LAT_TAG));
        intent.putExtra(MapUtility.PLACE_LNG_TAG, getArguments().getString(MapUtility.PLACE_LNG_TAG));
        intent.putExtra(MapUtility.PLACE_NAME_TAG, getArguments().getString(MapUtility.PLACE_NAME_TAG));
        intent.putExtra(MapUtility.CURRENT_DAY_TAG, getArguments().getString(MapUtility.CURRENT_DAY_TAG));
        intent.putExtra(MapUtility.PLACE_AUTO_TAG, true);

        recyclerView.setAdapter(new RecommendItemAdapter(getContext(), rec_items, R.layout.bottom_sheet_dialog, intent));

        return view;
    }

}
