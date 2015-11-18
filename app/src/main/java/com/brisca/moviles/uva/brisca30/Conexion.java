package com.brisca.moviles.uva.brisca30;

import android.app.Activity;
import android.content.Intent;
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

import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.brisca.moviles.uva.brisca30.Conect.ChatConnection;
import com.brisca.moviles.uva.brisca30.Conect.NsdHelper;

public class Conexion extends AppCompatActivity implements View.OnClickListener {
    TextView NombrePartida;
    NsdHelper mNsdHelper;

    private TextView mStatusView;
    private Handler mUpdateHandler;
    Button botonCrear;
    Button botonDescubrir;
    Button botonConectar;
    Button botonEnviar;
    public String TAG = "NsdChat";

    ChatConnection mConnection;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion);
        mStatusView = (TextView) findViewById(R.id.status);

        NombrePartida=(TextView)findViewById(R.id.NombrePartida);
        Bundle datos= getIntent().getExtras();
        NombrePartida.setText(datos.getString("nombrePartida"));
        NsdHelper.mServiceName=NsdHelper.mServiceName+datos.getString("nombrePartida");

        botonCrear=(Button) findViewById(R.id.advertise_btn);
        botonCrear.setOnClickListener(this);
        botonDescubrir=(Button) findViewById(R.id.discover_btn);
        botonDescubrir.setOnClickListener(this);
        botonConectar=(Button) findViewById(R.id.connect_btn);
        botonConectar.setOnClickListener(this);
        botonEnviar=(Button) findViewById(R.id.send_btn);
        botonEnviar.setOnClickListener(this);

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                addChatLine(chatLine);
            }
        };

        mConnection = new ChatConnection(mUpdateHandler);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();




        //crearServidor();
    }

    public void crear() {

        // Register service
        if(mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }



    public void descubrir() {
        mNsdHelper.discoverServices();
    }


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


    public void enviar() {
        EditText messageView = (EditText) this.findViewById(R.id.chatInput);
        if (messageView != null) {
            String messageString = messageView.getText().toString();
            if (!messageString.isEmpty()) {
                mConnection.sendMessage(messageString);
            }
            messageView.setText("");
        }
    }

    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
    }

    @Override
    protected void onPause() {
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }

    @Override
    protected void onDestroy() {
        mNsdHelper.tearDown();
        mConnection.tearDown();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.advertise_btn:
                crear();
                break;
            case R.id.discover_btn:
                descubrir();
                break;
            case R.id.connect_btn:
                conectar();
                break;
            case R.id.send_btn:
                enviar();
                break;
            default:
                break;
        }
    }
}
