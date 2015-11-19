package com.brisca.moviles.uva.brisca30.Juego;

/* Implementa una carta de la baraja */
public class Carta {

    private int numero;
    private String palo;

    /* Inicializa una nueva carta con el número y el palo indicados */
    public Carta(int numero, String palo){
        this.numero = numero;
        this.palo = palo;
    }

    /* Devuelve la puntuación de la carta para el conteo final */
    public int getPuntos() {
        int p = 0;
        switch (numero){
            case 1:
                p = 11;
                break;
            case 3:
                p = 10;
                break;
            case 10:
                p = 2;
                break;
            case 11:
                p = 3;
                break;
            case 12:
                p = 4;
                break;
            default:
                p = 0;
                break;
        }

        return p;
    }

    //Devuelve el palo de la carta
    public String getPalo(){
        return palo;
    }
}
