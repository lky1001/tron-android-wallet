package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Stat;
import com.devband.tronlib.dto.TronAccount;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 25..
 */

public class OverviewFragment extends CommonFragment implements OverviewView {

    @Inject
    OverviewPresenter mOverviewPresenter;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.block_height_title_text)
    TextView mBlockHeightTitleText;

    @BindView(R.id.block_height_text)
    TextView mBlockHeightText;

    @BindView(R.id.account_pie_chart)
    PieChart mAccountPieChart;

    public static Fragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);
        initUi();

        refresh();
        return view;
    }

    private void initUi() {
        //initialize PieChart
        mAccountPieChart.getDescription().setEnabled(false);
        mAccountPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mAccountPieChart.setDragDecelerationFrictionCoef(0.95f);
        mAccountPieChart.setCenterText(generateCenterSpannableText());

        mAccountPieChart.setDrawHoleEnabled(true);
        mAccountPieChart.setHoleColor(Color.WHITE);

        mAccountPieChart.setTransparentCircleColor(Color.WHITE);
        mAccountPieChart.setTransparentCircleAlpha(110);

        mAccountPieChart.setHoleRadius(58f);
        mAccountPieChart.setTransparentCircleRadius(61f);

        mAccountPieChart.setDrawCenterText(true);

        mAccountPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mAccountPieChart.setRotationEnabled(true);
        mAccountPieChart.setHighlightPerTapEnabled(false);

        mAccountPieChart.getLegend().setEnabled(false);

        mToolbarLayout.setTitle("");

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mBlockHeightTitleText.setVisibility(View.GONE);
                    mBlockHeightText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mBlockHeightTitleText.setVisibility(View.VISIBLE);
                    mBlockHeightText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    private void setTopAddressData(List<TronAccount> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mAccountPieChart.setUsePercentValues(false);
        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            TronAccount account = data.get(i);
            double balance = account.getBalance() / Constants.ONE_TRX;
            entries.add(new PieEntry((float) balance));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Top Address");

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData d = new PieData(dataSet);
        d.setValueFormatter(new DefaultValueFormatter(0));
        d.setValueTextSize(11f);
        d.setValueTextColor(Color.BLACK);
        mAccountPieChart.setData(d);

        // undo all highlights
        mAccountPieChart.highlightValues(null);

        mAccountPieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("TRON\nWallet Top 10 Address");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 4, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 4, s.length()-1, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 4, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), 4, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length()-14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void refresh() {
        if (isAdded()) {
            mOverviewPresenter.chartDataLoad();
        }
    }

    @Override
    public void overviewDataLoadSuccess(TronAccounts topAddressAccounts) {
        if (!isAdded()) {
            return;
        }
        hideDialog();
        setTopAddressData(topAddressAccounts.getData());
    }

    private void initLineChart(LineChart lineChart, List<Stat> stats) {
        if (!isAdded()) {
            return;
        }

        if (stats == null || stats.size() == 0) {
            return;
        }

        List<Entry> yVals = new ArrayList<>();

        Map<Integer, String> x = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < stats.size(); i++) {
            x.put(i, sdf.format(new Date(stats.get(i).getTimestamp())));
            yVals.add(new Entry((float) i, stats.get(i).getValue()));
        }

        LineDataSet set1;

        set1 = new LineDataSet(yVals, getString(R.string.avg_block_size_text));
        set1.setFillAlpha(110);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return x.get((int) value);
            }
        });

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        // set data
        lineChart.setData(data);

        lineChart.animateX(750);
        lineChart.invalidate();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
        }
    }
}
