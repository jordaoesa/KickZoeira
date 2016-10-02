package br.edu.ufcg.kickzoeira.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;
import br.edu.ufcg.kickzoeira.model.PerfilStatistic;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private final int PIC_CODE = 101;

    private View rootView;

    private OnFragmentInteractionListener mListener;

    private PieChart pie_chart;
    private RadarChart radar_chart;
    private Activity main_act;
    private Button btn_evaluate;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private ProgressBar progress_bar_apelido;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView apelido;

    private CircleImageView ivProfilePicture;

    private KickZoeiraUser currentUser;
    public static KickZoeiraUser observableUser = null;
    public static boolean isOnlyShow = false;

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

    public static ProfileFragment newInstance(KickZoeiraUser user) {
        observableUser = user;
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
        btn_evaluate = (Button) rootView.findViewById(R.id.button_evaluate);

        if(isOnlyShow) {
            currentUser = observableUser;
            btn_evaluate.setVisibility(View.GONE);
        }
        else if (observableUser != null){
            currentUser = observableUser;
            btn_evaluate.setVisibility(View.VISIBLE);
        }
        else{
            currentUser = new KickZoeiraUser(FirebaseAuth.getInstance().getCurrentUser());
            btn_evaluate.setVisibility(View.GONE);
        }

        ((KickZoeiraMainActivity)getActivity()).appBarLayout.setExpanded(true);
        ((KickZoeiraMainActivity)getActivity()).collapsingToolbar.setTitle("Perfil Zoeira");


        // TESTE

        this.pie_chart = (PieChart) rootView.findViewById(R.id.pie_chart);
        this.radar_chart = (RadarChart) rootView.findViewById(R.id.radar_chart);
        this.main_act = (KickZoeiraMainActivity)getActivity();


        String aplidoStr = currentUser.getApelido();

        apelido = (TextView) rootView.findViewById(R.id.text_profile_name);
        progress_bar_apelido = (ProgressBar)  rootView.findViewById(R.id.login_progress_apelido);


        System.out.println("#############################################################    onCreateView()");
        progress_bar_apelido.setVisibility(View.VISIBLE);




        FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(currentUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);

                String apelido_texto = user.getApelido();
                apelido.setVisibility(View.GONE);


                apelido.setText(user.getApelido() != null ? apelido_texto : "Apelido");

                progress_bar_apelido.setVisibility(View.GONE);
                apelido.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivProfilePicture = (CircleImageView) rootView.findViewById(R.id.pic_profile);
        if(observableUser == null) ivProfilePicture.setOnClickListener(onClick);
        retrieveProfilePicture();


        apelido.setText(aplidoStr != null ? aplidoStr : "Apelido");
        if(observableUser == null) apelido.setOnClickListener(onClick);

        progressDialog = new ProgressDialog(getActivity());


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(currentUser.getId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final KickZoeiraUser user = dataSnapshot.getValue(KickZoeiraUser.class);

                        final PerfilStatistic perfil_statistic = new PerfilStatistic(main_act,pie_chart, radar_chart, user);

                        btn_evaluate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog = new Dialog(main_act );
                                dialog.setContentView(R.layout.evaluation_activity);
                                dialog.setTitle("Zoação");
                                dialog.show();

                                Button btn_confirm_zoacao = (Button) dialog.findViewById(R.id.button_confirm_zoar);
                                btn_confirm_zoacao.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CheckBox caceteiro = (CheckBox) dialog.findViewById(R.id.checkBox_caceteiro);
                                        CheckBox brigao = (CheckBox) dialog.findViewById(R.id.checkBox_brigao);
                                        CheckBox reclamao = (CheckBox) dialog.findViewById(R.id.checkBox_reclamao);
                                        CheckBox fominha = (CheckBox) dialog.findViewById(R.id.checkBox_fominha);
                                        CheckBox enrolao = (CheckBox) dialog.findViewById(R.id.checkBox_enrolao);
                                        CheckBox preguica = (CheckBox) dialog.findViewById(R.id.checkBox_bixo_preguica);

                                        RatingBar posicionamento = (RatingBar) dialog.findViewById(R.id.ratingBar_posicionamento);
                                        RatingBar toque = (RatingBar) dialog.findViewById(R.id.ratingBar_toque);
                                        RatingBar dominio = (RatingBar) dialog.findViewById(R.id.ratingBar_dominio);
                                        RatingBar drible = (RatingBar) dialog.findViewById(R.id.ratingBar_drible);
                                        RatingBar defesa = (RatingBar) dialog.findViewById(R.id.ratingBar_defesa);
                                        RatingBar ataque = (RatingBar) dialog.findViewById(R.id.ratingBar_ataque);

                                        perfil_statistic.getStatistic_pie_chart().addAvaliacao(
                                                caceteiro.isChecked(),
                                                brigao.isChecked(),
                                                reclamao.isChecked(),
                                                fominha.isChecked(),
                                                enrolao.isChecked(),
                                                preguica.isChecked()
                                        );
                                        perfil_statistic.getStatistic_radar_chart().addAvaliacao(
                                                (int)posicionamento.getRating(),
                                                (int)toque.getRating(),
                                                (int)dominio.getRating(),
                                                (int)drible.getRating(),
                                                (int)defesa.getRating(),
                                                (int)ataque.getRating()
                                        );

                                        FirebaseDatabase.getInstance().getReference().child("kickzoeirauser").child(user.getId()).setValue(user);

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


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });



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


    private void updateProfilePicture() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.main_act, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }else {
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (it.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(it, PIC_CODE);
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (it.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(it, PIC_CODE);
                    }
                } else {
                    Snackbar.make(main_act.findViewById(android.R.id.content), "Vá para configurações e altere as permissões para usar a CAMERA!", Snackbar.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIC_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivProfilePicture.setImageBitmap(imageBitmap);
                saveProfilePicture(imageBitmap);
            }
        }
    }

    private void saveProfilePicture(Bitmap bitmap) {
        String path = "gs://kick-zoeira-6bec2.appspot.com/kickzoeirauser/{id}/profile.png";
        path = path.replace("{id}", currentUser.getId());

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageRef.putBytes(data);

//        UploadTask uploadTask = storageRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });
    }

    private void retrieveProfilePicture() {
        String path = "gs://kick-zoeira-6bec2.appspot.com/kickzoeirauser/{id}/profile.png";
        path = path.replace("{id}", currentUser.getId());
        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(path);

        islandRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivProfilePicture.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == ivProfilePicture.getId()) {
                updateProfilePicture();
            }
            else if(v.getId() == apelido.getId()) {
                updateApelido();
            }

        }
    };

    private void updateApelido(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Atualize seu Apelido?");

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String apelido_user = input.getText().toString();
                apelido.setText(apelido_user);
                progressDialog.show();
                progressDialog.setCancelable(false);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(apelido_user).build();

                KickZoeiraUser user_kick = new KickZoeiraUser(user);
                user_kick.setApelido(apelido_user);
                mDatabase.child("kickzoeirauser").child(user_kick.getId()).setValue(user_kick).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                    }
                });




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
