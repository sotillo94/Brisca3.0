package com.brisca.moviles.uva.brisca30.Baraja;

import java.util.ArrayList;

/* Implementa el monton de cartas que los jugadores han ganado a lo largo de la partida*/
public class Monton {

    private ArrayList<Carta> monton;

    /* Inicializa un nuevo Monton */
    public Monton(){
        monton = new ArrayList<Carta>();
    }

    //Añade una carta al monton
    public void meter(Carta c){
        monton.add(c);
    }

    //Saca una carta del monton
    private Carta sacar(){
        return monton.remove(0);
    }

    //Devuelve la puntuación total del monton
    public int contar(){
        int puntos = 0;
        Carta carta;

        while(!monton.isEmpty()){
            carta = sacar();
            puntos += carta.getPuntos();
        }

        return puntos;
    }
}
