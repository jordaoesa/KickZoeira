package br.edu.ufcg.kickzoeira.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.InputMismatchException;
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

    private List<Integer> pie_data = new ArrayList<>();
    private List<Integer> radar_data = new ArrayList<>();

    public KickZoeiraUser(String id, String email, String apelido, Uri photoUrl){
        this.id=id;
        this.email = email;
        this.apelido = apelido;
        this.photoUrl = photoUrl;
    }

    public KickZoeiraUser(){
        seguindo = new ArrayList<>();
        seguidores = new ArrayList<>();



        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(1);

        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(1);

    }

    public KickZoeiraUser(FirebaseUser user){
        seguindo = new ArrayList<>();
        seguidores = new ArrayList<>();

        this.id = user.getUid();
        this.email = user.getEmail();
        this.apelido = user.getDisplayName();
        this.photoUrl = user.getPhotoUrl();

        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(0);
        this.pie_data.add(1);

        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(0);
        this.radar_data.add(1);

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


    public List<Integer> getPie_data(){return this.pie_data;}

    public void setPie_data(List<Integer> new_pie_data){this.pie_data = new_pie_data;}

    public List<Integer> getRadar_data(){return this.radar_data;}

    public void setRadar_data(List<Integer> new_radar_data){this.radar_data = new_radar_data;}


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
