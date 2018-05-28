package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devband.tronlib.dto.TopAddressAccount;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 25..
 */

public class OverviewFragment extends BaseFragment implements OverviewView {

    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    public static BaseFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);
        initUi();
        mPresenter = new OverviewPresenter(this);

        refresh();
        return view;
    }

    private void initUi() {

        //initialize PieChart
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(false);

        mPieChart.getLegend().setEnabled(false);
    }

    private void setTopAddressData(List<TopAddressAccount> data) {
        mPieChart.setUsePercentValues(false);

        List<PieEntry> entries = new ArrayList<>();

        for (int i=2; i<data.size(); i++) {
            TopAddressAccount account = data.get(i);
            double balance = account.getBalance() / Constants.ONE_TRX;
            entries.add(new PieEntry((float) balance));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Top Adress");

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
        mPieChart.setData(d);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("TRON\nWallert Top 15 Address");
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
        ((OverviewPresenter) mPresenter).dataLoad();
    }

    @Override
    public void overviewDataLoadSuccess(TopAddressAccounts topAddressAccounts) {
        hideDialog();
        setTopAddressData(topAddressAccounts.getData());
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(getActivity(), getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }
}
