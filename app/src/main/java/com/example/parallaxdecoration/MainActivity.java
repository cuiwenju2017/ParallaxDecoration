package com.example.parallaxdecoration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity {

    private final int[] bgs = new int[]{R.drawable.rd_gua_seed_1, R.drawable.rd_gua_seed_2, R.drawable.rd_gua_seed_3,
            R.drawable.rd_gua_seed_4, R.drawable.rd_gua_seed_5, R.drawable.rd_gua_seed_6};
    private MyAdapter listAdapter;
    private RecyclerView.ItemDecoration lastItemDecoration;
    private float parallaxSize = 1.0F;
    private boolean autoFillBitmap;
    private RecyclerView recycler_view;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listAdapter = new MyAdapter(this);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager((RecyclerView.LayoutManager) (new LinearLayoutManager((Context) this, RecyclerView.HORIZONTAL, false)));
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(listAdapter);

        Context context = recycler_view.getContext();
        ParallaxDecoration parallaxDecoration = new ParallaxDecoration(context);
        parallaxDecoration.setupResource(ArraysKt.asList(this.bgs));
        parallaxDecoration.setParallax(this.parallaxSize);
        parallaxDecoration.setAutoFill(this.autoFillBitmap);
        this.lastItemDecoration = (RecyclerView.ItemDecoration) parallaxDecoration;

        RecyclerView.ItemDecoration itemDecoration = this.lastItemDecoration;
        recycler_view.addItemDecoration(itemDecoration);
        updateItemDecoration(true);
        parallaxSize = (float) 50 * 1.0F / (float) 100;
        updateItemDecoration(autoFillBitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private final void updateItemDecoration(boolean isAutoFill) {
        RecyclerView recyclerView;
        RecyclerView.ItemDecoration itemDecoration;
        if (this.lastItemDecoration != null) {
            recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
            itemDecoration = this.lastItemDecoration;
            Intrinsics.checkNotNull(itemDecoration);
            recyclerView.removeItemDecoration(itemDecoration);
        }

        autoFillBitmap = isAutoFill;
        ParallaxDecoration parallaxDecoration = new ParallaxDecoration((Context) this);
        parallaxDecoration.setupResource(ArraysKt.asList(bgs));
        parallaxDecoration.setParallax(parallaxSize);
        parallaxDecoration.setAutoFill(autoFillBitmap);
        lastItemDecoration = (RecyclerView.ItemDecoration) parallaxDecoration;
        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        itemDecoration = lastItemDecoration;
        Intrinsics.checkNotNull(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
    }
}