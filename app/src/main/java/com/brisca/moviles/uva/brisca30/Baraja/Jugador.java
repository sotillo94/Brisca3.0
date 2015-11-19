package com.brisca.moviles.uva.brisca30.Baraja;

/* Implementa un jugador de la partida */
public class Jugador {

    String nombre;
    private Carta[] mano;

    //Inicializa un jugador con el nombre indicado */
    public Jugador(String nombre){
        this.nombre = nombre;
        mano = new Carta[3];
    }

    //Devuelve la primera posición del array mano que está vacía
    private int posicionVacia(Carta[] mano){
        if(mano[0] == null){
            return 0;
        }
        else{
            if(mano[1] == null){
                return 1;
            }
            else{
                return 2;
            }
        }
    }

    //Añade una carta a la mano del jugador
    public void robar(Carta c){
        int pos = posicionVacia(mano);
        mano[pos] = c;
    }

    //Devuelve la carta indicada y la elimina de la mano del jugador
    public Carta echar(int i){
        Carta c = mano[i];
        mano[i] = null;
        return c;
    }
}