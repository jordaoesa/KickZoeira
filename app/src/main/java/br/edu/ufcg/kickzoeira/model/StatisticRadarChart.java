package br.edu.ufcg.kickzoeira.model;

import android.graphics.Color;
import android.util.Log;

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

public class StatisticRadarChart {

    private KickZoeiraUser user;

    private int posicionamento = 0;
    private int toque = 0;
    private int dominio = 0;
    private int drible = 0;
    private int defesa = 0;
    private int ataque = 0;

    private int total_avaliations =1;

    private float[] values_data = new float[6];
    private final String[] labels = {"Posicionamento", "Toque", "Domínio", "Drible", "Defesa", "Ataque"};

    public RadarChart radar_chart;

    public StatisticRadarChart(RadarChart radarchart, KickZoeiraUser user){
        this.radar_chart = radarchart;
        this.user = user;

        List<Integer> votes_count  = user.getPie_data();
        this.posicionamento = votes_count.get(0);
        this.toque = votes_count.get(1);
        this.dominio = votes_count.get(2);
        this.drible = votes_count.get(3);
        this.defesa = votes_count.get(4);
        this.ataque = votes_count.get(5);
        this.total_avaliations = votes_count.get(6);



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

        user.setRadar_data(votes_count);

    }

    private void update_data(){
        this.values_data[0] = this.posicionamento/this.total_avaliations;
        this.values_data[1] = this.toque/this.total_avaliations;
        this.values_data[2] = this.dominio/this.total_avaliations;
        this.values_data[3] = this.drible/this.total_avaliations;
        this.values_data[4] = this.defesa/this.total_avaliations;
        this.values_data[5] = this.ataque/this.total_avaliations;
        this.addData();
    }

    private void addData(){
        ArrayList<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < this.values_data.length; i++) {
            entries.add(new RadarEntry(this.values_data[i],this.values_data[i]));
        }

        RadarDataSet data_set = new RadarDataSet(entries, "Eficácia");

        data_set.setColor(Color.GRAY);
        data_set.setFillColor(Color.LTGRAY);

        data_set.setDrawFilled(true);

        ArrayList<String> labels2 = new ArrayList<>();

        for (int i = 0; i < this.labels.length; i++) {
            labels2.add(this.labels[i]);
            Log.d("labels",labels2.get(i));
        }


        RadarData data = new RadarData();
        data.addDataSet(data_set);
        data.setLabels(labels2);

        this.radar_chart.setData(data);

        this.radar_chart.setDescription("");


        this.radar_chart.setWebColor(Color.GRAY);
        this.radar_chart.setDrawingCacheBackgroundColor(Color.RED);
        this.radar_chart.setWebColorInner(Color.RED);


        XAxis xAxis = this.radar_chart.getXAxis();
        xAxis.setTextSize(9f);
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




        Legend l = this.radar_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);


        this.radar_chart.invalidate();


    }




}
