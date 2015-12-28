package com.brisca.moviles.uva.brisca30;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brisca.moviles.uva.brisca30.ConectCliente.ChatConnection;
import com.brisca.moviles.uva.brisca30.ConectCliente.NsdHelper;

import static android.os.SystemClock.sleep;

/**
 * Activity para la crear la nueva partida
 *
 * Esta actividad sirve para crear una partida a la que despues se van a conectar los clientes
 *
 */
public class UnirseNuevaPartida extends AppCompatActivity implements View.OnClickListener {
    Button botonNombrePartida;
    TextView textNuevaPartida;
    TextView titulo;
    //Estado de la conexion


    //Chat
    private TextView mStatusView;

    NsdHelper mNsdHelper;
    public String TAG = "NsdChat";
    ChatConnection mConnection;

    int primeraVez=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_nueva_partida);

        botonNombrePartida=(Button) findViewById(R.id.buttonNombrePartida);
        botonNombrePartida.setOnClickListener(this);

        if (primeraVez==1){
            //Ponemos como nombre del servicio el nombre que introdujo el usuario en la actividad anterior
            //
            // NsdHelper.mServiceName=NsdHelper.mServiceName+datos.getString("nombrePartida");
            //
            //Recoge los mensajes enviados
            Handler mUpdateHandler;
            //Aqui recibimos datos
            mUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String chatLine = msg.getData().getString("msg");
                    addChatLine(chatLine);
                }
            };

            //inicializamos el chat
            mConnection = new ChatConnection(mUpdateHandler);
            //inicializamos la conexion
            mNsdHelper = new NsdHelper(this);
            //la conexion se inicia
            mNsdHelper.initializeNsd();

            descubrir();
            sleep(1000);
            mNsdHelper.stopDiscovery();
            sleep(1000);
            primeraVez++;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**Creamos nueva partida*/
            case R.id.buttonNombrePartida:
                textNuevaPartida=(TextView) findViewById(R.id.mens_nombre_partida);
                titulo=(TextView) findViewById(R.id.titulo);
                String nombre=  textNuevaPartida.getText().toString();
                Bundle datos;

                Intent intentCrearPartida = new Intent(UnirseNuevaPartida.this,Cliente.class);
                datos=new Bundle();
                datos.putString("nombrePartida",nombre);
                intentCrearPartida.putExtras(datos);
                startActivity(intentCrearPartida);
                break;
            default:
                break;
        }
    }

    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
    }

    public void descubrir() {
        mNsdHelper.discoverServices();
    }
}
