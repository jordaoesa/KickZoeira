package br.edu.ufcg.kickzoeira.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordaoesa on 9/28/16.
 */
public class KickZoeiraUser {

    private String id;
    private String email;
    private String apelido;
    private Uri photoUrl;

    private List<String> seguindo;
    private List<String> seguidores;

    public KickZoeiraUser(){
        seguindo = new ArrayList<>();
        seguidores = new ArrayList<>();
    }

    public KickZoeiraUser(FirebaseUser user){
        seguindo = new ArrayList<>();
        seguidores = new ArrayList<>();

        this.id = user.getUid();
        this.email = user.getEmail();
        this.apelido = user.getDisplayName();
        this.photoUrl = user.getPhotoUrl();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(List<String> seguindo) {
        this.seguindo = seguindo;
    }

    public List<String> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<String> seguidores) {
        this.seguidores = seguidores;
    }

    public void addSeguidor(String userId){
        this.seguidores.add(userId);
    }
    public void removeSeguidor(String userId){
        this.seguidores.remove(userId);
    }
    public void addSeguindo(String userId){
        this.seguindo.add(userId);
    }
    public void removeSeguind(String userId){
        this.seguindo.remove(userId);
    }

}
