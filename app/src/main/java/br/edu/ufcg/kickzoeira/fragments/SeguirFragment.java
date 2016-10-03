package br.edu.ufcg.kickzoeira.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.adapters.SeguirAdapter;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeguirFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeguirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeguirFragment extends Fragment {

    private View rootView;

    private RecyclerView recyclerView;
    private SeguirAdapter arrayAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private List<KickZoeiraUser> users;
    private DatabaseReference mDatabase;

    private OnFragmentInteractionListener mListener;

    public SeguirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SeguirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeguirFragment newInstance() {
        SeguirFragment fragment = new SeguirFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.profile, menu);
        final MenuItem item = menu.findItem(R.id.search_item);
        final SearchView searchView = new SearchView(((KickZoeiraMainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(item, searchView);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(false);
            }
        });
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewCollapsed();
                item.collapseActionView();
                ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seguir, container, false);

        users = new ArrayList<KickZoeiraUser>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.seguir_recycler_view);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kickzoeirauser").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<HashMap<String, KickZoeiraUser>> list = new GenericTypeIndicator<HashMap<String, KickZoeiraUser>>() {};
                        final HashMap<String, KickZoeiraUser> map = dataSnapshot.getValue(list);

                        mDatabase.child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                List<String> followingEmails = new ArrayList<String>();
                                KickZoeiraUser currUser = dataSnapshot.getValue(KickZoeiraUser.class);
                                for (String info : currUser.getSeguindo()){
                                    String[] temp = info.split("\\|");
                                    followingEmails.add(temp[1]);
                                }

                                List<KickZoeiraUser> allUsers = new ArrayList<KickZoeiraUser>();
                                for(String key : map.keySet()){
                                    if(!map.get(key).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !followingEmails.contains(map.get(key).getEmail())){
                                        allUsers.add(map.get(key));
                                    }
                                }

                                arrayAdapter = new SeguirAdapter(allUsers, getContext(), currUser);
                                recyclerView.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mDatabase.child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                mDatabase.child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        List<String> followingEmails = new ArrayList<String>();
                                        KickZoeiraUser currUser = dataSnapshot.getValue(KickZoeiraUser.class);
                                        for (String info : currUser.getSeguindo()){
                                            String[] temp = info.split("\\|");
                                            followingEmails.add(temp[1]);
                                        }

                                        List<KickZoeiraUser> allUsers = new ArrayList<KickZoeiraUser>();
                                        for(String key : map.keySet()){
                                            if(!map.get(key).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !followingEmails.contains(map.get(key).getEmail())){
                                                allUsers.add(map.get(key));
                                            }
                                        }

                                        arrayAdapter = new SeguirAdapter(allUsers, getContext(), currUser);
                                        recyclerView.setAdapter(arrayAdapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE_WARNING", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Seguindo Zoeiros");

        ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.GONE);
        ((KickZoeiraMainActivity)getContext()).fabSacanear.setVisibility(View.GONE);

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
