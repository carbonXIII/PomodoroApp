package org.team.app.view;

import org.team.app.contract.StatsContract;

import android.os.Bundle;
import android.view.View;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.components.AxisBase;

import java.util.Map;
import java.util.ArrayList;

public class StatsView extends FragmentView implements StatsContract.View {
    protected StatsContract.Presenter mPresenter;

    protected PieChart chart;

    public StatsView() {
        super(R.layout.screen_stats);
    }

    @Override
    public void setPresenter(StatsContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setData(Map<String, Long> inputData) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(Map.Entry<String, Long> p: inputData.entrySet())
            entries.add(new PieEntry(p.getValue(), p.getKey()));

        PieDataSet dataSet = new PieDataSet(entries, "Work Time by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new TimeFormatter());
        data.setValueTextSize(14f);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.chart = view.findViewById(R.id.pieChart);
        chart.getDescription().setEnabled(false);
        chart.setEntryLabelTextSize(14f);
        chart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
    }

    class TimeFormatter extends ValueFormatter {
        public TimeFormatter() {
            super();
        }

        @Override
        public String getFormattedValue(float value) {
            int millis = (int) value;
            if(millis < 60 * 1000)
                return "" + millis / 1000 + " sec";
            if(millis < 60 * 60 * 1000)
                return "" + millis / 60 / 1000 + " min";
            return "" + millis / 60 / 60 / 1000 + " hr";
        }
    }
}
