package br.edu.ufcg.kickzoeira.model;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
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

    public StatisticPieChart statistic_pie_chart;
    public StatisticRadarChart statistic_radar_chart;

    public PerfilStatistic(final Activity mainAct, PieChart piechart, RadarChart radarchart, KickZoeiraUser user){
        this.statistic_pie_chart = new StatisticPieChart(mainAct, piechart, user);
        this.statistic_radar_chart = new StatisticRadarChart(radarchart, user);
    }

    public StatisticPieChart getStatistic_pie_chart(){
        return this.statistic_pie_chart;
    }

    public StatisticRadarChart getStatistic_radar_chart(){
        return this.statistic_radar_chart;
    }




}
