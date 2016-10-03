package br.edu.ufcg.kickzoeira.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

/**
 * Created by jordaoesa on 10/2/16.
 */

public class SeguirAdapter extends RecyclerView.Adapter<SeguirAdapter.UserHolder> {

    private Context context;
    private KickZoeiraUser currentUser;
    private List<KickZoeiraUser> users;

    public SeguirAdapter(List<KickZoeiraUser> users, Context context, KickZoeiraUser currentUser) {
        this.users = users;
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public SeguirAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user, parent, false);
        return new SeguirAdapter.UserHolder(v, context, currentUser);
    }

    @Override
    public void onBindViewHolder(SeguirAdapter.UserHolder holder, int position) {
        KickZoeiraUser user = users.get(position);
        holder.tvApelido.setText(user.getApelido() != null ? user.getApelido() : "Apelido");
        holder.tvEmail.setText(user.getEmail());
        holder.user = user;
        if (user.getPhotoUrl() == null) {
            retrieveProfilePicture(holder.ivUser, user);
        } else {
            holder.ivUser.setImageURI(user.getPhotoUrl());
        }
    }

    private void retrieveProfilePicture(final ImageView iv, KickZoeiraUser user) {
        String path = "gs://kick-zoeira-6bec2.appspot.com/kickzoeirauser/{id}/profile.png";
        path = path.replace("{id}", user.getId());
        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(path);

        islandRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {

        public ImageView ivUser;
        public TextView tvApelido;
        public TextView tvEmail;
        public KickZoeiraUser user;

        public UserHolder(final View itemView, final Context context, final KickZoeiraUser currentUser) {
            super(itemView);
            ivUser = (ImageView) itemView.findViewById(R.id.profile_image);
            tvApelido = (TextView) itemView.findViewById(R.id.tv_apelido);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Deseja seguir '" + user.getEmail() + "'?");

                    builder.setPositiveButton("seguir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            currentUser.addSeguindo(user.getId() + "|" + user.getEmail() + "|" + (user.getApelido() != null ? user.getApelido() : "Apelido"));

                            FirebaseDatabase.getInstance().getReference("kickzoeirauser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "seguindo '" + user.getEmail() + "'.", Toast.LENGTH_LONG).show();
                                }
                            });



                        }
                    });
                    builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
    }
}