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

/**
 * Created by YURISNMELO on 9/28/2016.
 */

public class StatisticRadarChart {

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

    public StatisticRadarChart(RadarChart radarchart){
        this.radar_chart = radarchart;
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

        Log.d("avaliacao","AVALIACAO RADAR");
        Log.d("avaliacao",String.valueOf(this.posicionamento));
        Log.d("avaliacao",String.valueOf(this.toque));
        Log.d("avaliacao",String.valueOf(this.dominio));
        Log.d("avaliacao",String.valueOf(this.drible));
        Log.d("avaliacao",String.valueOf(this.defesa));
        Log.d("avaliacao",String.valueOf(this.ataque));

        this.update_data();

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
