package br.edu.ufcg.kickzoeira.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
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

        PerfilStatistic perfil_statistic = new PerfilStatistic(this.main_act,this.pie_chart, this.radar_chart);

        ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.VISIBLE);
        ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fbShareDeficiencia = rootView.findViewById(R.id.fb_share_deficiencia);
                fbShareDeficiencia.setBackgroundColor(Color.WHITE);
                View fbShareHumilhacao = rootView.findViewById(R.id.fb_share_humilhacao);
                fbShareHumilhacao.setBackgroundColor(Color.WHITE);
                ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.GONE);

                fbShareDeficiencia.setDrawingCacheEnabled(true);
                Bitmap image = fbShareDeficiencia.getDrawingCache();

                fbShareHumilhacao.setDrawingCacheEnabled(true);
                Bitmap image2 = fbShareHumilhacao.getDrawingCache();

                SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
                SharePhoto photo2 = new SharePhoto.Builder().setBitmap(image2).build();
                ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.VISIBLE);

                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).addPhoto(photo2).build();
                ShareDialog.show(getActivity(), content);
                fbShareDeficiencia.setBackgroundColor(Color.TRANSPARENT);
                fbShareHumilhacao.setBackgroundColor(Color.TRANSPARENT);
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
