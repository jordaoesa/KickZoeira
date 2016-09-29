package br.edu.ufcg.kickzoeira.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.adapters.FollowersAdapter;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeguidoresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeguidoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeguidoresFragment extends Fragment {

    private View rootView;
    private GridView gridView;
    private FollowersAdapter arrayAdapter;
    private List<KickZoeiraUser> users;
    private OnFragmentInteractionListener mListener;
    private FirebaseDatabase mDatabase;

    public SeguidoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SeguidoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeguidoresFragment newInstance() {
        SeguidoresFragment fragment = new SeguidoresFragment();
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
        rootView = inflater.inflate(R.layout.fragment_seguidores, container, false);

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
//        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Seguidores Zoeiros");

//        final String userId = getUid();

//        context = this;
//        eventId = getIntent().getStringExtra("event_id");
//        eventUsers = new ArrayList<BackendlessUser>();
//        groupUsers = new ArrayList<BackendlessUser>();
        users = new ArrayList<KickZoeiraUser>();


        FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);
                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        System.out.println(user.getEmail());
//                        System.out.println(user.getSeguidores());
//                        Toast.makeText(getActivity().getApplicationContext(), user.getSeguidores().get(0), Toast.LENGTH_SHORT).show();


                        for (String info : user.getSeguidores()){
                            String[] temp = info.split("|");
                            users.add(new KickZoeiraUser(temp[0],temp[1],temp[2],null));
                        }

                        gridView = (GridView) getView().findViewById(R.id.gridView);
                        arrayAdapter = new FollowersAdapter(getContext(), R.layout.grid_view_followers_select, users);

                        gridView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
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
