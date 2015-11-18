package com.brisca.moviles.uva.brisca30;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button botonReglas;
    Button botonInstrucciones;
    Button botonCrearPartida;
    Button botonUnirPartida;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonReglas=(Button) findViewById(R.id.buttonReglas);
        botonReglas.setOnClickListener(this);

        botonInstrucciones=(Button) findViewById(R.id.buttonInstrucciones);
        botonInstrucciones.setOnClickListener(this);

        botonCrearPartida=(Button) findViewById(R.id.buttonCrearPartida);
        botonCrearPartida.setOnClickListener(this);

        botonUnirPartida=(Button) findViewById(R.id.buttonUnirPartida);
        botonUnirPartida.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonReglas:
                Intent intentReglas = new Intent(MainActivity.this,ReglasBrisca.class);
                startActivity(intentReglas);
                break;
            case R.id.buttonInstrucciones:
                Intent intentInstrucciones = new Intent(MainActivity.this,InstruccionesBrisca.class);
                startActivity(intentInstrucciones);
                break;
            case R.id.buttonCrearPartida:
                Intent intentCrearPartida = new Intent(MainActivity.this,CrearNuevaPartida.class);

                Bundle datos=new Bundle();
                datos.putBoolean("crear",true);
                intentCrearPartida.putExtras(datos);

                startActivity(intentCrearPartida);
                break;
            case R.id.buttonUnirPartida:
                Intent intentUnirPartida = new Intent(MainActivity.this,CrearNuevaPartida.class);

                Bundle datos2=new Bundle();
                datos2.putBoolean("crear",false);
                intentUnirPartida.putExtras(datos2);

                startActivity(intentUnirPartida);
                break;
            default:
                break;
        }
    }
}
