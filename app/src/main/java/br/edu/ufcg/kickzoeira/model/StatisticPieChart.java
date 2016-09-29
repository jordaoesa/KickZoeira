package br.edu.ufcg.kickzoeira.model;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

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

import br.edu.ufcg.kickzoeira.KickZoeiraApplication;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;

/**
 * Created by YURISNMELO on 9/28/2016.
 */

public class StatisticPieChart {

    private int caceteiro = 17;
    private int brigao = 25;
    private int reclamao = 10;
    private int fominha = 40;
    private int enrolao = 31;
    private int morto = 55;

    private int total_avaliations =100;

    private float[] y_data = new float[6];
    private final String[] v_data = {"Caceteiro", "Brigão", "Reclamão", "Fominha", "Enrolão", "Morto"};

    private PieChart pie_chart;

    public StatisticPieChart(final Activity mainAct, PieChart piechart){
        this.pie_chart = piechart;
        this.update_y_data();

        this.pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e==null)return;
                PieEntry e2 = (PieEntry) e;
                Log.d("onvalueselected", e.getClass().toString());
                Toast.makeText(mainAct, e2.getLabel()+ " = " + String.valueOf(e.getY()) + "%",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addData();

        Legend l = this.pie_chart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

    }

    public void addAvaliacao(boolean c, boolean b, boolean r, boolean f, boolean e, boolean m){
        if(c) this.caceteiro++;
        if(b) this.brigao++;
        if(r) this.reclamao++;
        if(f) this.fominha++;
        if(e) this.enrolao++;
        if(m) this.morto++;
        this.total_avaliations++;

        this.update_y_data();
    }

    private void update_y_data(){
        this.y_data[0] = this.caceteiro*100/this.total_avaliations;
        this.y_data[1] = this.brigao*100/this.total_avaliations;
        this.y_data[2] = this.reclamao*100/this.total_avaliations;
        this.y_data[3] = this.fominha*100/this.total_avaliations;
        this.y_data[4] = this.enrolao*100/this.total_avaliations;
        this.y_data[5] = this.morto*100/this.total_avaliations;
    }

    private void addData(){

        ArrayList<PieEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < this.y_data.length; i++) {
            yVals1.add(new PieEntry(this.y_data[i], this.v_data[i]));
        }

        ArrayList<String> vVals = new ArrayList<>();

        for (int i = 0; i < this.v_data.length; i++) {
            vVals.add(this.v_data[i]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Deficiência");

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

        this.pie_chart.setDescription("");

        this.pie_chart.setData(data);

        this.pie_chart.highlightValues(null);

        this.pie_chart.invalidate();



    }
}