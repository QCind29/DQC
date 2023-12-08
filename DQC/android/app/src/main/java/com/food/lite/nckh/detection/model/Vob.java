package com.food.lite.nckh.detection.model;

public class Vob {


    int idVob;
    String tendoituongTA;
    String tendoituongTV;
    String datcauTA;
    String datcauTV;
    String audioEN;
    String audioVN;

    public Vob(String audioEN, String audioVN) {
        this.audioEN = audioEN;
        this.audioVN = audioVN;
    }



    public String getAudioEN() {
        return audioEN;
    }

    public void setAudioEN(String audioEN) {
        this.audioEN = audioEN;
    }

    public String getAudioVN() {
        return audioVN;
    }

    public void setAudioVN(String audioVN) {
        this.audioVN = audioVN;
    }

    public void setDatcauTA(String datcauTA) {
        this.datcauTA = datcauTA;
    }

    public void setDatcauTV(String datcauTV) {
        this.datcauTV = datcauTV;
    }


    public String getDatcauTA() {
        return datcauTA;
    }

    public String getDatcauTV() {
        return datcauTV;
    }


    public Vob(int idVob, String tendoituongTA, String tendoituongTV, String datcauTA, String datcauTV) {
        this.idVob = idVob;
        this.tendoituongTA = tendoituongTA;
        this.tendoituongTV = tendoituongTV;
        this.datcauTA = datcauTA;
        this.datcauTV = datcauTV;

    }

    public Vob(){};

    public int getIdVob() {
        return idVob;
    }

    public void setIdVob(int idVob) {
        this.idVob = idVob;
    }

    public String getTendoituongTA() {
        return tendoituongTA;
    }

    public void setTendoituongTA(String tendoituongTA) {
        this.tendoituongTA = tendoituongTA;
    }

    public String getTendoituongTV() {
        return tendoituongTV;
    }

    public void setTendoituongTV(String tendoituongTV) {
        this.tendoituongTV = tendoituongTV;
    }




}
