package br.edu.ufcg.kickzoeira.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.model.PerfilStatistic;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private View rootView;

    private OnFragmentInteractionListener mListener;

    private PieChart pie_chart;
    private RadarChart radar_chart;
    private Activity main_act;
    private Button btn_evaluate;
    private Dialog dialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Perfil Zoeira");

        // TESTE

        this.pie_chart = (PieChart) rootView.findViewById(R.id.pie_chart);
        this.radar_chart = (RadarChart) rootView.findViewById(R.id.radar_chart);
        this.main_act = (KickZoeiraMainActivity)getActivity();
        this.btn_evaluate = (Button) rootView.findViewById(R.id.button_evaluate);

        this.dialog = new Dialog(this.main_act , android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.dialog.setContentView(R.layout.custom_dialog);
        this.dialog.setTitle("Zoação");

        final PerfilStatistic perfil_statistic = new PerfilStatistic(this.main_act,this.pie_chart, this.radar_chart);

        this.btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Button btn_confirm_zoacao = (Button) dialog.findViewById(R.id.button_confirm_zoar);
                btn_confirm_zoacao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean caceteiro = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();
                        boolean brigao = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();
                        boolean reclamao = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();
                        boolean fominha = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();
                        boolean enrolao = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();
                        boolean preguica = dialog.findViewById(R.id.checkBox_caceteiro).isEnabled();

                        RatingBar posicionamento = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                        RatingBar toque = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                        RatingBar dominio = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                        RatingBar drible = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                        RatingBar defesa = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                        RatingBar ataque = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);

                        perfil_statistic.getStatistic_pie_chart().addAvaliacao(
                                caceteiro,brigao,reclamao,fominha,enrolao,preguica
                        );
                        perfil_statistic.getStatistic_radar_chart().addAvaliacao(
                                (int)posicionamento.getRating(),
                                (int)toque.getRating(),
                                (int)dominio.getRating(),
                                (int)drible.getRating(),
                                (int)defesa.getRating(),
                                (int)ataque.getRating()
                        );

                        dialog.dismiss();
                    }
                });

                TextView cancelar = (TextView) dialog.findViewById(R.id.text_cancelar_zoacao);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });




        return rootView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
