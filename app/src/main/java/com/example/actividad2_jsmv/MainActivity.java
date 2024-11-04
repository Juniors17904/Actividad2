package com.example.actividad2_jsmv;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.actividad2_jsmv.modelo.dao.ProyectoDAO;
import com.example.actividad2_jsmv.modelo.dto.Proyecto;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnFocusChangeListener{

    private TextInputEditText txtCodProyecto, txtCodActividad, txtObservacion, txtFecha;
    private Spinner spEstado;
    private Button btnFecha, btnRegistrar, btnEliminar ,btnBuscar;
    private ListView listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Main", "Iniciando");
        enlazarControles();
        fecha();
        configurarBotones();
        listar();
        cargarEstados();
        listado.setOnItemClickListener(this);
    }

    private void enlazarControles()  {
        Log.i("Main", "Enlazar Controles");
        txtCodProyecto = findViewById(R.id.txtCodProyecto);
        txtCodActividad = findViewById(R.id.txtCodActividad);
        txtObservacion = findViewById(R.id.txtObservacion);
        txtFecha = findViewById(R.id.txtFecha);
        spEstado = findViewById(R.id.spEstado);
        btnFecha = findViewById(R.id.btnFecha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnEliminar = findViewById(R.id.btnEliminar);
        listado = findViewById(R.id.listado);
        btnBuscar=findViewById(R.id.btnBuscar);

    }

    private void mostrarCalendario() {
        Log.i("Main", "mostrar Calendario");
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFecha.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void listar() {

        Log.i("Main", "listando en List view");

        ProyectoDAO pDAO = new ProyectoDAO(this);
        List<Proyecto> lista = pDAO.getList();

        ArrayAdapter<Proyecto> adp = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lista);
        listado.setAdapter(adp); // el adaptador al ListView
    }

    private void registrar(View v) {
        ocultarTeclado();
        Log.i("Main", "registrar");

        if (txtCodProyecto.getText().toString().isEmpty()||
           txtCodActividad.getText().toString().isEmpty()||
           txtObservacion.getText().toString().isEmpty()||
           txtFecha.getText().toString().isEmpty()) {
            Snackbar.make(v, "Campos Vacios", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.rojo)).show();
            return;
        }
        ProyectoDAO pDAO = new ProyectoDAO(this);
        Proyecto p = new Proyecto();
        p.setCodigoProyecto(Integer.parseInt(txtCodProyecto.getText().toString()));
        p.setCodigoActividad(Integer.parseInt(txtCodActividad.getText().toString()));
        p.setEstado(spEstado.getSelectedItem().toString());
        p.setObservacion(txtObservacion.getText().toString());
        p.setFecha(txtFecha.getText().toString());

        String resp = "";
        if (btnRegistrar.getText().toString().equals(getResources().getString(R.string.lbl_Btn_Registar))) {
            resp = pDAO.insertar(p);
        } else {
            resp = pDAO.update(p);
        }if (resp.isEmpty()) {
            Snackbar.make(v, "Grabado correctamente", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.azul)).show();
            limpiarCajas(true);
            listar();
        } else {
            Snackbar.make(v, resp, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.rojo)).show();
        }
        Log.i("Infox", resp);


    }

    private void limpiarCajas(boolean b) {
        txtCodProyecto.setText("");
        txtCodActividad.setText("");
        txtObservacion.setText("");
        txtFecha.setText("");
        spEstado.setSelection(0);
        btnRegistrar.setText(getResources().getString(R.string.lbl_Btn_Registar));
        txtCodProyecto.requestFocus();
        ocultarTeclado();
        fecha();
    }

    public void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this); // Vista por defecto si no hay foco actual
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void cargarEstados() {
        Log.i("Main", "Cargar Estados");

        ProyectoDAO pDAO = new ProyectoDAO(this);
        ArrayList<String> estados = pDAO.obtenerEstados();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estados);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterSpinner);
    }

    private void configurarBotones() {
        Log.i("Main", "Configurar Botones");
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar(v);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               eliminar(v);
            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            buscar(v);
            }
        });

    }

    private void buscar(View v) {
        Log.i("Main","Buscar");
        final EditText buscar = new EditText(MainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Buscar");
        builder.setMessage("Codigo de Proyecto");
        builder.setView(buscar);
        buscar.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String CodP=buscar.getText().toString();
                Log.i("Main","Buscar:"+CodP);
                if(!CodP.isEmpty()){
                    try {
                        int id = Integer.parseInt(CodP);
                        ProyectoDAO pDAO = new ProyectoDAO(MainActivity.this);
                        Proyecto p = pDAO.buscar(id);
                        if (p == null) {
                            Snackbar.make(v, "No existe el codigo Proyecto "+ CodP, Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getColor(R.color.rojo)).show();

                        } else {
                            mostrar(p);
                            Snackbar.make(v, "Registros encontrados", Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getColor(R.color.azul)).show();

                        }
                    }catch(SQLException ex){
                        Log.i("Main","No se encontro busqueda");
                        Snackbar.make(v, ex.getMessage(), Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getColor(R.color.rojo)).show();

                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ocultarTeclado();
                        }
                    }, 100);

                }
            }
        });
        builder.show();
    }

    private void eliminar(View V) {
        String codigoP = txtCodProyecto.getText().toString().trim();

        if (codigoP.isEmpty()) {
            Snackbar.make(V, "Se necesita el Código del Proyecto", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.rojo)).show();
            return;
        }

        int id = Integer.parseInt(codigoP);
        ProyectoDAO pDAO = new ProyectoDAO(this);
        String resp = pDAO.eliminar(id);

        if (resp.isEmpty()) {
            Snackbar.make(V, "Registro eliminado ", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.azul)).show();
        } else {
            Snackbar.make(V, resp, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getColor(R.color.rojo)).show();
        }

        listar();
        limpiarCajas(true);
    }

    private void mostrar(Proyecto p) {
        Log.i("Main","mostrar");
        if (p != null) {
            txtCodProyecto.setText(String.valueOf(p.getCodigoProyecto()));
            txtCodActividad.setText(String.valueOf(p.getCodigoActividad()));
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spEstado.getAdapter();
            int position = adapter.getPosition(p.getEstado());
            spEstado.setSelection(position);
            txtObservacion.setText(p.getObservacion());
            txtFecha.setText(p.getFecha());

            btnRegistrar.setText(getResources().getString(R.string.lbl_Btn_Actualizar));
        }
    }

    private void fecha() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        txtFecha.setText(String.format("%02d/%02d/%d", day, month + 1, year));
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void onItemClick(AdapterView<?> adp, View view, int position, long id) {
        Proyecto p =(Proyecto) adp.getItemAtPosition(position);
        Log.i("Main","Codigo Proyecto Seleccionado:  " +String.valueOf(p.getCodigoProyecto()));
        mostrar(p);
    }


}
