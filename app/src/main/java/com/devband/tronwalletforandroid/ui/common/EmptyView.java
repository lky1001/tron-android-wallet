package com.devband.tronwalletforandroid.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;

/**
 * Created by user on 2018. 5. 31..
 */

public class EmptyView extends LinearLayout {

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Empty_View, defStyleAttr, 0);
        int titleResId = a.getResourceId(R.styleable.Empty_View_empty_title, R.string.empty);

        initUi(context, titleResId);
    }

    private void initUi(Context context, int titleResId) {
        //parent setting
        setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        ImageView imgLogo = new ImageView(context);
        imgLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.tron));

        TextView txtTitle = new TextView(context);
        txtTitle.setText(titleResId);

        addView(imgLogo, lp);
        addView(txtTitle, lp);
    }
}
