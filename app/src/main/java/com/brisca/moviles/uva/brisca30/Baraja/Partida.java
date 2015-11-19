package com.brisca.moviles.uva.brisca30.Baraja;

/* Implementa una partida de brisca */
public class Partida {

    private Baraja baraja;
    private Equipo[] equipos = new Equipo[2];
    private Jugador[] listajug;
    private int juegos;
    private int tamEq;
    private String paloTriunfo;
    private int numJugadorMano;

    /* Inicializa una partida con los equipos, el numero de juegos indicados y le asigna una baraja.
    El número de integrantes del equipo debe ser el mismo.
     */
    public Partida(Equipo eq1, Equipo eq2, int jue){
        if(eq1.getTamEquipo() == eq2.getTamEquipo()) {
            equipos[0] = eq1;
            equipos[1] = eq2;
            juegos = jue;
            baraja = new Baraja();
            tamEq = eq1.getTamEquipo();
            listajug = new Jugador[2*tamEq];

            //Array con la lista de jugadores alternados por equipos
            for(int i = 0; i < tamEq; i++){
                listajug[i*2] = eq1.jugadores[i];
                listajug[(i*2)+1] = eq2.jugadores[i];
            }

            //Asigna aleatoriamente el jugador mano
            numJugadorMano = (int) Math.floor(Math.random()*tamEq*2);
        }
    }

    //Reparte 3 cartas a cada jugador al inicial la partida
    public void repartir(){
        int i, j;

        baraja.mezclar();

        for(i = 0; i < 3; i++){
            for(j = 0; j < listajug.length; j++){
                listajug[numJugadorMano].robar(baraja.saca());
                numJugadorMano = (numJugadorMano++)%listajug.length;//vuelve a 0 cuando roba el
                                                                    // ultimo jugador de la lista
            }
        }
    }

    //Asigna el palo que es triunfo y devuelve la primera carta de la baraja
    public Carta asignarTriunfo(){
        Carta c = baraja.saca();
        paloTriunfo = c.getPalo();
        return c;
    }



}
