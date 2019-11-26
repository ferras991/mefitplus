package com.example.exercicio3_imc.Class;

import java.io.Serializable;

public class User implements Serializable {

    //user properties
    private String id;
    private String name;
    private double weight;
    private double height;
    private String birthDate;
    private int gender;

    public User() {

    }

    //user constructor
    public User(String id, String name, double weight, double height, String birthDate, int gender) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.birthDate = birthDate;
        this.gender = gender;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getbirthDate() {
        return birthDate;
    }

    public void setbirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }



    public double calculateImc(double weight, double height) {
        double imc = weight / (height * height);
        return Math.round(imc * 10.0) / 10.0;
    }

    public double calculatePMin (double height) {
        double pmin = height * height * 20;
        return Math.round(pmin * 10.0) / 10.0;
    }

    public double calculatePMax (double height) {
        double pmax =  height * height * 25;
        return Math.round(pmax * 10.0) / 10.0;
    }

    public double calculateIg(int birthDate, int gender, double imc) {
        double ig;

        if (birthDate < 18) {
            ig = (1.51*imc - 0.7*birthDate - 3.6*gender);
        } else {
            ig = (1.2*imc + 0.23*birthDate - 10.8*gender - 5.4);
        }

        return Math.round(ig * 10.0) / 10.0;
    }

    public double calculateMg(double weight, double ig) {
        double mg = (weight * ig) / 100;
        return Math.round(mg * 10.0) / 10.0;
    }

    public double calculateAt(int gender, int birthDate, double height, double weight) {
        double at;

        if (gender == 1) {
            at = (2.447 - (0.09156*birthDate) + (0.1074*height*100) + (0.3362*weight));
        } else {
            at = (2.079 + (0.1069*height*100) + (0.2246*weight));
        }

        return Math.round(at * 10.0) / 10.0;
    }

    public double calculateMineral(double weight, double mg, double at) {
        double mineral = (weight - (mg + at)) * 1/3;
        return Math.round(mineral * 10.0) / 10.0;
    }

    public double calculateProteina(double weight, double mg, double at) {
        double proteina = (weight - (mg + at)) * 2/3;
        return Math.round(proteina * 10.0) / 10.0;
    }
}
