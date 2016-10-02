package br.edu.ufcg.kickzoeira.model;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
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
import java.util.List;

import br.edu.ufcg.kickzoeira.KickZoeiraApplication;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;

/**
 * Created by YURISNMELO on 9/28/2016.
 */

public class StatisticPieChart {

    private KickZoeiraUser user;

    private int caceteiro = 0;
    private int brigao = 0;
    private int reclamao = 0;
    private int fominha = 0;
    private int enrolao = 0;
    private int morto = 0;

    private int total_avaliations =1;

    private float[] y_data = new float[6];
    private final String[] v_data = {"Caceteiro", "Brigão", "Reclamão", "Fominha", "Enrolão", "Morto"};

    private PieChart pie_chart;

    public StatisticPieChart(final Activity mainAct, PieChart piechart, KickZoeiraUser user){
        this.pie_chart = piechart;
        this.user = user;

        List<Integer> votes_count  = user.getPie_data();
        this.caceteiro = votes_count.get(0);
        this.brigao = votes_count.get(1);
        this.reclamao = votes_count.get(2);
        this.fominha = votes_count.get(3);
        this.enrolao = votes_count.get(4);
        this.morto = votes_count.get(5);
        this.total_avaliations = votes_count.get(6);


        this.update_y_data();

        this.pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e==null)return;
                PieEntry e2 = (PieEntry) e;
                Toast.makeText(mainAct, e2.getLabel()+ " = " + String.valueOf(e.getY()) + "%",
                        Toast.LENGTH_SHORT).show();


//                Snackbar.make(mainAct.findViewById(android.R.id.content), e2.getLabel()+ " = " + String.valueOf(e.getY()) + "%", Snackbar.LENGTH_LONG)
//                        .show();
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

        List<Integer> votes_count  = new ArrayList<>();
        votes_count.add(this.caceteiro);
        votes_count.add(this.brigao);
        votes_count.add(this.reclamao);
        votes_count.add(this.fominha);
        votes_count.add(this.enrolao);
        votes_count.add(this.morto);
        votes_count.add(this.total_avaliations);

        user.setPie_data(votes_count);
    }

    private void update_y_data(){
        this.y_data[0] = this.caceteiro*100/this.total_avaliations;
        this.y_data[1] = this.brigao*100/this.total_avaliations;
        this.y_data[2] = this.reclamao*100/this.total_avaliations;
        this.y_data[3] = this.fominha*100/this.total_avaliations;
        this.y_data[4] = this.enrolao*100/this.total_avaliations;
        this.y_data[5] = this.morto*100/this.total_avaliations;
        this.addData();
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
        dataSet.setSelectionShift(10);
        dataSet.setValueTextColor(Color.BLACK);

        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> colors2 = new ArrayList<>();

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
        colors2.add(Color.rgb(29,233,182));
        colors2.add(Color.rgb(0,229,255));
        colors2.add(Color.rgb(255,61,0));
        colors2.add(Color.rgb(101,31,255));
        colors2.add(Color.rgb(255,64,129));
        colors2.add(Color.rgb(96,125,139));
        dataSet.setColors(colors2);


        PieData data = new PieData(dataSet);


        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);


        this.pie_chart.setDescription("");

        this.pie_chart.setData(data);

        this.pie_chart.highlightValues(null);

        this.pie_chart.invalidate();



    }
}
