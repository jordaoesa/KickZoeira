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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.adapters.SeguidoresAdapter;
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

    private RecyclerView recyclerView;
    private SeguidoresAdapter arrayAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private View rootView;
    private List<KickZoeiraUser> users;
    private OnFragmentInteractionListener mListener;

    private static KickZoeiraMainActivity main_act;

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


        main_act = (KickZoeiraMainActivity) getActivity();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);
                        List<KickZoeiraUser> availableUsers = new ArrayList<KickZoeiraUser>();
                        for (String info : user.getSeguidores()){
                            String[] temp = info.split("\\|");
                            availableUsers.add(new KickZoeiraUser(temp[0],temp[1],temp[2],null));
                        }

                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                //TODO
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                List<KickZoeiraUser> usersToShow = new ArrayList<KickZoeiraUser>();
                                for(KickZoeiraUser usr : users){
                                    if(usr.getEmail().contains(newText)){
                                        usersToShow.add(usr);
                                    }
                                }
                                arrayAdapter = new SeguidoresAdapter(usersToShow, main_act.getApplicationContext());
                                recyclerView.setAdapter(arrayAdapter);
                                return true;
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE_WARNING", "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seguidores, container, false);
        users = new ArrayList<KickZoeiraUser>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.seguidores_recycler_view);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        main_act = (KickZoeiraMainActivity) getActivity();

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Seguidores Zoeiros");

        ((KickZoeiraMainActivity)getContext()).fabFacebookShare.setVisibility(View.GONE);
        ((KickZoeiraMainActivity)getContext()).fabSacanear.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);

                        for (String info : user.getSeguidores()){
                            String[] temp = info.split("\\|");
                            users.add(new KickZoeiraUser(temp[0],temp[1],temp[2],null));
                        }

                        arrayAdapter = new SeguidoresAdapter(users, main_act.getApplicationContext());
                        recyclerView.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("FIREBASE_WARNING", "getUser:onCancelled", databaseError.toException());
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
