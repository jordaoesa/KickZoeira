package br.edu.ufcg.kickzoeira.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.adapters.SeguidoresAdapter;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimularFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimularFragment extends Fragment {

    private View rootView;
    private Button gerarProbabilidade;
    private Button time1;
    private Button time2;
    private List<String> time1_apelidos;
    private List<String> time2_apelidos;
    HashMap<String, Integer> mapaJogadores1;
    HashMap<String, Integer> mapaJogadores2;
    private int media1 = 0;
    private int media2 = 0;

    private OnFragmentInteractionListener mListener;

    public SimularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SimularFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimularFragment newInstance() {
        SimularFragment fragment = new SimularFragment();
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
        rootView = inflater.inflate(R.layout.fragment_simular, container, false);

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Simular Zoeira");

        ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.GONE);

        mapaJogadores1 = new HashMap<String, Integer>();
        mapaJogadores2 = new HashMap<String, Integer>();
        time1_apelidos = new ArrayList<String>();
        time2_apelidos = new ArrayList<String>();

        gerarProbabilidade = (Button) rootView.findViewById(R.id.gerarProb);

        time1 = (Button) rootView.findViewById(R.id.time1);
        time2 = (Button) rootView.findViewById(R.id.time2);

        gerarProbabilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapaJogadores1.isEmpty() || mapaJogadores2.isEmpty()){
                    Snackbar.make(getView(), "Escolha os times primeiro", Snackbar.LENGTH_SHORT);
                }
                else{

                    for (String k : time2_apelidos){
                       media2 += mapaJogadores2.get(k);
                    }

                    for (String k : time1_apelidos){
                        media1 += mapaJogadores1.get(k);
                    }
                    Snackbar.make(rootView, media1 + " " + media2, Snackbar.LENGTH_SHORT);
                }
            }
        });

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);
                                final List<String> apelidos = new ArrayList<String>();

                                for (String info : user.getSeguidores()) {
                                    String[] temp = info.split("\\|");
                                    int soma_habilidades = Integer.parseInt(temp[3])+Integer.parseInt(temp[4])+Integer.parseInt(temp[5])+Integer.parseInt(temp[6])
                                            +Integer.parseInt(temp[7])+Integer.parseInt(temp[8]);
                                    mapaJogadores1.put(temp[2],soma_habilidades);
                                    apelidos.add(temp[2]);
                                }
                                for (String info : user.getSeguindo()) {
                                    String[] temp = info.split("\\|");
                                    int soma_habilidades = Integer.parseInt(temp[3])+Integer.parseInt(temp[4])+Integer.parseInt(temp[5])+Integer.parseInt(temp[6])
                                            +Integer.parseInt(temp[7])+Integer.parseInt(temp[8]);
                                    mapaJogadores1.put(temp[2],soma_habilidades);
                                    apelidos.add(temp[2]);
                                }
                                System.out.print(apelidos);
                                final CharSequence[] items=  apelidos.toArray(new CharSequence[apelidos.size()]);
                                final ArrayList<Integer> seletedItems = new ArrayList<Integer>();

                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setTitle("Selecione Jogadores")
                                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                                if (isChecked) {

                                                    seletedItems.add(indexSelected);
                                                } else if (seletedItems.contains(indexSelected)) {

                                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                                }
                                            }
                                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                    for (int i = 0; i < seletedItems.size(); i++){
                                                        int index = seletedItems.get(i);
                                                        time1_apelidos.add(items[index]+"");
                                                    }

                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        }).create();
                                dialog.show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("FIREBASE_WARNING", "getUser:onCancelled", databaseError.toException());
                                // ...
                            }
                        });


            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);
                                final List<String> apelidos = new ArrayList<String>();
                                for (String info : user.getSeguidores()) {
                                    String[] temp = info.split("\\|");
                                    int soma_habilidades = Integer.parseInt(temp[3])+Integer.parseInt(temp[4])+Integer.parseInt(temp[5])+Integer.parseInt(temp[6])
                                            +Integer.parseInt(temp[7])+Integer.parseInt(temp[8]);
                                    mapaJogadores2.put(temp[2],soma_habilidades);
                                    apelidos.add(temp[2]);
                                }
                                for (String info : user.getSeguindo()) {
                                    String[] temp = info.split("\\|");
                                    int soma_habilidades = Integer.parseInt(temp[3])+Integer.parseInt(temp[4])+Integer.parseInt(temp[5])+Integer.parseInt(temp[6])
                                            +Integer.parseInt(temp[7])+Integer.parseInt(temp[8]);
                                    mapaJogadores2.put(temp[2],soma_habilidades);
                                    apelidos.add(temp[2]);
                                }

                                final CharSequence[] items=  apelidos.toArray(new CharSequence[apelidos.size()]);
                                final ArrayList<Integer> seletedItems = new ArrayList<Integer>();

                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setTitle("Selecione Jogadores")
                                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                                if (isChecked) {

                                                    seletedItems.add(indexSelected);
                                                } else if (seletedItems.contains(indexSelected)) {

                                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                                }
                                            }
                                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                for (int i = 0; i < seletedItems.size(); i++){
                                                    int index = seletedItems.get(i);
                                                    time2_apelidos.add(items[index]+"");
                                                }
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        }).create();
                                dialog.show();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("FIREBASE_WARNING", "getUser:onCancelled", databaseError.toException());
                                // ...
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
