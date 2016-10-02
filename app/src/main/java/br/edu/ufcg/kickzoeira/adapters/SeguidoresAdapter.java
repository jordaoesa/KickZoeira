package br.edu.ufcg.kickzoeira.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.activities.KickZoeiraMainActivity;
import br.edu.ufcg.kickzoeira.fragments.ProfileFragment;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

/**
 * Created by jordaoesa on 10/1/16.
 */

public class SeguidoresAdapter extends RecyclerView.Adapter<SeguidoresAdapter.UserHolder> {

    private List<KickZoeiraUser> users;

    public SeguidoresAdapter(List<KickZoeiraUser> users){
        this.users = users;
    }

    @Override
    public SeguidoresAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user, parent, false);
        return new SeguidoresAdapter.UserHolder(v);
    }

    @Override
    public void onBindViewHolder(SeguidoresAdapter.UserHolder holder, int position) {
        KickZoeiraUser user = users.get(position);
        holder.tvApelido.setText(user.getApelido());
        holder.tvEmail.setText(user.getEmail());
        holder.user = user;
        if(user.getPhotoUrl() == null){
            retrieveProfilePicture(holder.ivUser, user);
        }else{
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

    public static class UserHolder extends RecyclerView.ViewHolder{

        public ImageView ivUser;
        public TextView tvApelido;
        public TextView tvEmail;
        public KickZoeiraUser user;

        public UserHolder(final View itemView) {
            super(itemView);
            ivUser = (ImageView) itemView.findViewById(R.id.profile_image);
            tvApelido = (TextView) itemView.findViewById(R.id.tv_apelido);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileFragment fragment = ProfileFragment.newInstance(user);
                    KickZoeiraMainActivity activity = (KickZoeiraMainActivity)itemView.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, fragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
