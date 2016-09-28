package br.edu.ufcg.kickzoeira.model;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import br.edu.ufcg.kickzoeira.R;

/**
 * Created by YURISNMELO on 9/28/2016.
 */

public class PerfilStatistic{

    private float[] y_data = {5,10,15,30,40};
    private final String[] v_data = {"Drible", "Passe", "Defesa", "Ataque", "Agilidade"};
    private PieChart pie_chart;


    public PerfilStatistic(PieChart piechart){
        this.pie_chart = piechart;

        pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e==null)return;
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addData();

        Legend l = pie_chart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    private void addData(){
        ArrayList<PieEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < this.y_data.length; i++) {
            yVals1.add(new PieEntry(this.y_data[i], i));
        }

        ArrayList<String> vVals = new ArrayList<>();

        for (int i = 0; i < this.v_data.length; i++) {
            vVals.add(this.v_data[i]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Labs");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        this.pie_chart.setData(data);

        this.pie_chart.highlightValues(null);

        this.pie_chart.invalidate();



    }

}
