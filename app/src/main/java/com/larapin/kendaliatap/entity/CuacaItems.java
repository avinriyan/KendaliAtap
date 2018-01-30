package com.larapin.kendaliatap.entity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asus on 25/12/2017.
 */

public class CuacaItems {
    private int id;
    private String cuaca;
    private String waktu;
    private String suhu;
    private String kelembaban;
    private String atap;
    private String mode;

    public CuacaItems(JSONObject object){
        try {
            int id = object.getInt("id");
            String cuaca = object.getString("cuaca");
            String waktu = object.getString("waktu");
            String suhu = object.getString("suhu");
            String kelembaban = object.getString("kelembaban");
            String atap = object.getString("atap");
            String mode = object.getString("mode");

            //mencari hari
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inFormat.parse(waktu);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, d MMMM yyyy hh:mm:ss");
            String time = outFormat.format(date);

            this.id = id;
            this.cuaca = cuaca;
            this.waktu = time;
            this.suhu = "Suhu: "+suhu+"°C";
            this.kelembaban = "Kelembaban: "+kelembaban+"%";
            this.mode = "Mode: "+mode;
            this.atap = "Atap: "+atap;

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

    public String getCuaca() {
        return cuaca;
    }

    public void setCuaca(String cuaca) {
        this.cuaca = cuaca;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getSuhu() {
        return suhu;
    }

    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getKelembaban() {
        return kelembaban;
    }

    public void setKelembaban(String kelembaban) {
        this.kelembaban = kelembaban;
    }

    public String getAtap() {
        return atap;
    }

    public void setAtap(String atap) {
        this.atap = atap;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
