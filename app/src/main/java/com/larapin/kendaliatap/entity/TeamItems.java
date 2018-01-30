package com.larapin.kendaliatap.entity;

import org.json.JSONObject;

/**
 * Created by asus on 24/12/2017.
 */

public class TeamItems {
    private int id;
    private String nim;
    private String nama;
    private String foto;

    public TeamItems(JSONObject object){
        try {
            int id = object.getInt("id");
            String nim = object.getString("nim");
            String nama = object.getString("nama");
            String foto = object.getString("foto");

            this.id = id;
            this.nim = nim;
            this.nama = nama;
            this.foto = foto;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
