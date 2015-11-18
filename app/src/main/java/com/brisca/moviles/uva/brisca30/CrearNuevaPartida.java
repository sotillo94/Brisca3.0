package com.brisca.moviles.uva.brisca30;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CrearNuevaPartida extends AppCompatActivity implements View.OnClickListener {
    Button botonNombrePartida;
    TextView textNuevaPartida;
    TextView titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nueva_partida);

        botonNombrePartida=(Button) findViewById(R.id.buttonNombrePartida);
        botonNombrePartida.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonNombrePartida:
                textNuevaPartida=(TextView) findViewById(R.id.mens_nombre_partida);
                titulo=(TextView) findViewById(R.id.titulo);
                String nombre=  textNuevaPartida.getText().toString();

                Bundle datos= getIntent().getExtras();
                Boolean crear= datos.getBoolean("crear");

                Intent intentCrearPartida = new Intent(CrearNuevaPartida.this,Conexion.class);
                datos=new Bundle();
                datos.putString("nombrePartida",nombre);
                datos.putBoolean("crear",crear);
                intentCrearPartida.putExtras(datos);
                startActivity(intentCrearPartida);
                break;
            default:
                break;
        }
    }




}
