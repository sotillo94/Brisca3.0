package com.brisca.moviles.uva.brisca30;


import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brisca.moviles.uva.brisca30.Conect.ChatConnection;
import com.brisca.moviles.uva.brisca30.Conect.MyParcelable;
import com.brisca.moviles.uva.brisca30.Conect.NsdHelper;

/**
 * Esta clase es la encargada de mostrar y controlar la interfaz del chat.
 * Además inicia el servidio en caso de ser servidor y busca y conecta a servicios en caso de ser cliente.
 */
public class Servidor extends AppCompatActivity implements View.OnClickListener {

    //Utilizado para mostrar el estado de la conexion
    TextView estadoPartida;

    //Estado de la conexion
    NsdHelper mNsdHelper;

    MyParcelable parcelable = new MyParcelable();
    //Chat
    private TextView mStatusView;

    Button botonEnviar;
    Button botonParar;
    public String TAG = "NsdChat";

    //Estado del chat
    ChatConnection mConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_servidor);
        //accedemos a los campos del layout
        mStatusView = (TextView) findViewById(R.id.status);
        estadoPartida=(TextView)findViewById(R.id.NombrePartida);

        botonEnviar=(Button) findViewById(R.id.send_btn);
        botonEnviar.setOnClickListener(this);//Le activamos el onClickListener
        botonParar=(Button) findViewById(R.id.stop_btn);
        botonParar.setOnClickListener(this);//Le activamos el onClickListener

        //Recogemos los datos que nos ha pasado la activity anterior
        Bundle datos= getIntent().getExtras();
        estadoPartida.setText(datos.getString("nombrePartida"));


        //Ponemos como nombre del servicio el nombre que introdujo el usuario en la actividad anterior
        NsdHelper.mServiceName=NsdHelper.mServiceName+datos.getString("nombrePartida");
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

        NsdHelper.TAG +="Servidor";
        ChatConnection.TAG +="Servidor";

            //Si es servidor debe crear un servicio, se le anade Serv al final para que no se intente conectar así mismo
            NsdHelper.mServiceName=NsdHelper.mServiceName+"Serv";
            crear();
            estadoPartida.setText("Creado");


    }

    /**
     * Crea un nuevo servicio
     */
    public void crear() {

        // Register service
        if(mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }


    /**
     * Busca servicios a los que conectarse
     */
    public void descubrir() {
        mNsdHelper.discoverServices();
    }

    /**
     * Se conecta al servicio con mismo nombre
     */
    public void conectar() {
        NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
        if (service != null) {
            Log.d(TAG, "Connecting.");
            mConnection.connectToServer(service.getHost(),
                    service.getPort());

        } else {
            Log.d(TAG, "No service to connect to!");
        }
    }

    /**
     * envia la cadena de texto que aparece en el editText del layout
     * si esta vacio envia "Enviar vacio"
     */
    public void enviar() {
        EditText messageView = (EditText) this.findViewById(R.id.chatInput);
        if (messageView != null) {
            String messageString = messageView.getText().toString();
            if (!messageString.isEmpty()) {
                mConnection.sendMessage(messageString);
            }
        }
    }

    /**
     * envia la cadena de texto introducida como parametro
     * @param x es el mensaje que se desea enviar
     */
    public void enviar(String x) {
        EditText messageView = (EditText) this.findViewById(R.id.chatInput);
        if (messageView != null) {
            mConnection.sendMessage(x);
            messageView.setText("");
        }
    }

    /**
     * Es el encargado de mostrar en el chat el mensaje recibido
     * @param line mensaje que recibimos y sera mostrado
     */
    public void addChatLine(String line) {
        if(line.equals("FIN")){
            fin();
        }
        mStatusView.append("\n" + line);
    }

    @Override
    protected void onPause() {
        /*
        if (mNsdHelper != null) {
            mNsdHelper.tearDown();
        }
        super.onPause();
        */
        //fin();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();/*
        if (mNsdHelper != null) {
            mNsdHelper.registerService(mConnection.getLocalPort());
            mNsdHelper.discoverServices();
        }*/
    }

    @Override
    protected void onDestroy() {
        //fin();
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.send_btn:
                //si pulsamos el boton enviar se llama a enviar()
                enviar();
                break;
            case R.id.stop_btn:
                //si pulsamos el boton enviar se llama a enviar()
                enviar("FIN");
                fin();
                break;
            default:
                break;
        }

    }
    public void fin(){
        mNsdHelper.tearDown();
        mConnection.tearDown();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putCharSequence("TEXT", mStatusView.getText());
        outState.putCharSequence("ESTADO", estadoPartida.getText());
        parcelable.setmConnection(mConnection);
        parcelable.setmNsdHelper(mNsdHelper);
        outState.putParcelable("Parce",parcelable);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mStatusView = (TextView) findViewById(R.id.status);
        mStatusView.setText(String.valueOf(savedInstanceState.getCharSequence("TEXT")));
        estadoPartida.setText(String.valueOf(savedInstanceState.getCharSequence("ESTADO")));
        parcelable=savedInstanceState.getParcelable("Parce");
        mConnection=parcelable.getmConnection();
        mNsdHelper=parcelable.getmNsdHelper();
    }

}
/*
//Estado de la conexion
NsdHelper mNsdHelper;
ChatConnection mConnection;
*/