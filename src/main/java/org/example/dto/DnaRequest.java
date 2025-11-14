package org.example.dto;

public class DnaRequest {
    private String[] dna;

    // Getters y Setters (Spring los necesita para funcionar)
    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

}
