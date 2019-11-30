package com.example.exercicio3_imc.Class;

import java.io.Serializable;

public class Imc implements Serializable {

    private String time;
    private String date;
    private double imc;
    private double imcPmax;
    private double imcPmin;
    private double imcIg;
    private double imcMg;
    private double imcAt;
    private double imcMineral;
    private double imcProtein;

    public Imc() {
    }

    public Imc(String time, String date, double imc, double imcPmax, double imcPmin, double imcIg, double imcMg, double imcAt, double imcMineral, double imcProtein) {
        this.time = time;
        this.date = date;
        this.imc = imc;
        this.imcPmax = imcPmax;
        this.imcPmin = imcPmin;
        this.imcIg = imcIg;
        this.imcMg = imcMg;
        this.imcAt = imcAt;
        this.imcMineral = imcMineral;
        this.imcProtein = imcProtein;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public double getImcPmax() {
        return imcPmax;
    }

    public void setImcPmax(double imcPmax) {
        this.imcPmax = imcPmax;
    }

    public double getImcPmin() {
        return imcPmin;
    }

    public void setImcPmin(double imcPmin) {
        this.imcPmin = imcPmin;
    }

    public double getImcIg() {
        return imcIg;
    }

    public void setImcIg(double imcIg) {
        this.imcIg = imcIg;
    }

    public double getImcMg() {
        return imcMg;
    }

    public void setImcMg(double imcMg) {
        this.imcMg = imcMg;
    }

    public double getImcAt() {
        return imcAt;
    }

    public void setImcAt(double imcAt) {
        this.imcAt = imcAt;
    }

    public double getImcMineral() {
        return imcMineral;
    }

    public void setImcMineral(double imcMineral) {
        this.imcMineral = imcMineral;
    }

    public double getImcProtein() {
        return imcProtein;
    }

    public void setImcProtein(double imcProtein) {
        this.imcProtein = imcProtein;
    }
}
