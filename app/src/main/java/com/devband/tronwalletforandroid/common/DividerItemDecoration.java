package com.devband.tronwalletforandroid.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int spaceFixer;

    public DividerItemDecoration(int spaceFixer) {
        this.spaceFixer = spaceFixer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1) {
            outRect.bottom = this.spaceFixer;
        }
    }
}