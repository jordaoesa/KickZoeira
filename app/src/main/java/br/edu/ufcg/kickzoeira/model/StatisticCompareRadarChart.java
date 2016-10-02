package br.edu.ufcg.kickzoeira.model;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YURISNMELO on 9/28/2016.
 */
public class StatisticCompareRadarChart {
    private KickZoeiraUser me;
    private KickZoeiraUser other;

    private int posicionamento = 0;
    private int toque = 0;
    private int dominio = 0;
    private int drible = 0;
    private int defesa = 0;
    private int ataque = 0;

    private int posicionamento2 = 0;
    private int toque2 = 0;
    private int dominio2 = 0;
    private int drible2 = 0;
    private int defesa2 = 0;
    private int ataque2 = 0;

    private int total_avaliations =1;
    private int total_avaliations2 =1;

    private float[] values_data = new float[6];
    private float[] values_data2 = new float[6];
    private final String[] labels = {"Posicionamento", "Toque", "Domínio", "Drible", "Defesa", "Ataque"};

    public RadarChart radar_chart;

    public StatisticCompareRadarChart(RadarChart radarchart, KickZoeiraUser me, KickZoeiraUser other){
        this.radar_chart = radarchart;
        this.me = me;
        this.other = other;

        List<Integer> votes_count  = me.getRadar_data();
        this.posicionamento = votes_count.get(0);
        this.toque = votes_count.get(1);
        this.dominio = votes_count.get(2);
        this.drible = votes_count.get(3);
        this.defesa = votes_count.get(4);
        this.ataque = votes_count.get(5);
        this.total_avaliations = votes_count.get(6);

        List<Integer> votes_count2 = other.getRadar_data();
        this.posicionamento = votes_count2.get(0);
        this.toque = votes_count2.get(1);
        this.dominio = votes_count2.get(2);
        this.drible = votes_count2.get(3);
        this.defesa = votes_count2.get(4);
        this.ataque = votes_count2.get(5);
        this.total_avaliations = votes_count2.get(6);



        this.update_data();

        this.addData();

    }

    public void addAvaliacao(int p,int t,int d,int dr,int de,int a){
        this.posicionamento+=p;
        this.toque+=t;
        this.dominio+=d;
        this.drible+=dr;
        this.defesa+=de;
        this.ataque+=a;
        this.total_avaliations++;

        this.update_data();

        List<Integer> votes_count  = new ArrayList<>();
        votes_count.add(this.posicionamento);
        votes_count.add(this.toque);
        votes_count.add(this.dominio);
        votes_count.add(this.drible);
        votes_count.add(this.defesa);
        votes_count.add(this.ataque);
        votes_count.add(this.total_avaliations);

        me.setRadar_data(votes_count);

    }

    private void update_data(){
        this.values_data[0] = this.posicionamento/this.total_avaliations;
        this.values_data[1] = this.toque/this.total_avaliations;
        this.values_data[2] = this.dominio/this.total_avaliations;
        this.values_data[3] = this.drible/this.total_avaliations;
        this.values_data[4] = this.defesa/this.total_avaliations;
        this.values_data[5] = this.ataque/this.total_avaliations;

        this.values_data2[0] = this.posicionamento2/this.total_avaliations2;
        this.values_data2[1] = this.toque2/this.total_avaliations2;
        this.values_data2[2] = this.dominio2/this.total_avaliations2;
        this.values_data2[3] = this.drible2/this.total_avaliations2;
        this.values_data2[4] = this.defesa2/this.total_avaliations2;
        this.values_data2[5] = this.ataque2/this.total_avaliations2;

        this.addData();
    }

    private void addData(){
        ArrayList<RadarEntry> entries = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();

        for (int i = 0; i < this.values_data.length; i++) {
            entries.add(new RadarEntry(this.values_data[i],this.values_data[i]));
            entries2.add(new RadarEntry(this.values_data2[i],this.values_data2[i]));
        }

        RadarDataSet data_set = new RadarDataSet(entries, "Minha Eficácia");
        RadarDataSet data_set2 = new RadarDataSet(entries2, "Eficácia do Zoado");

        data_set.setColor(Color.rgb(0,77,64));
        data_set.setValueTextColor(Color.BLACK);
        data_set.setFillColor(Color.rgb(100,255,218));
        data_set.setFillAlpha(150);
        data_set.setDrawFilled(true);


        data_set2.setColor(Color.rgb(0,77,64));
        data_set2.setValueTextColor(Color.BLACK);
        data_set2.setFillColor(Color.rgb(229,57,53));
        data_set2.setFillAlpha(150);
        data_set2.setDrawFilled(true);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<String> labels2 = new ArrayList<>();

        for (int i = 0; i < this.labels.length; i++) {
            labels.add(this.labels[i]);
            labels2.add(this.labels[i]);
        }


        RadarData data = new RadarData();

        data.addDataSet(data_set);
        data.addDataSet(data_set2);
        data.setLabels(labels2);
        data.setValueTextSize(9f);


        this.radar_chart.setData(data);
        this.radar_chart.setDescription("");
//        this.radar_chart.setBackgroundColor(Color.rgb(60, 65, 82));
        this.radar_chart.setWebLineWidth(1f);
        this.radar_chart.setWebColor(Color.rgb(0,77,64));
        this.radar_chart.setWebLineWidthInner(1f);
        this.radar_chart.setWebColorInner(Color.rgb(0,77,64));
        this.radar_chart.setWebAlpha(100);

        this.radar_chart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);


        XAxis xAxis = this.radar_chart.getXAxis();
        xAxis.setTextSize(7f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);

        xAxis.setValueFormatter(new AxisValueFormatter() {
            private String[] mActivities = new String[]{"Posicionamento", "Toque", "Domínio", "Drible", "Defesa", "Ataque"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });





        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = this.radar_chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(80f);
        yAxis.setDrawLabels(false);

//        Legend l = this.radar_chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(5f);
//        l.setTextColor(Color.WHITE);


        this.radar_chart.invalidate();


    }




}

