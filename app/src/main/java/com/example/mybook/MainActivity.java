package com.example.mybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Vistas
    EditText mTitleEt, mDescriptionEt;
    Button mSaveBtn, mListBtn;

    //Progreso del dialogo
    ProgressDialog pd;

    //instancia del firestore
    FirebaseFirestore db;

    String pId, pTitle, pDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        //action de carga con el titulo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Añadir libro");

        //Inicializando las vistas con xml
        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mListBtn = findViewById(R.id.listBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //actualizar
            actionBar.setTitle( "Actualizar" );
            mSaveBtn.setText( "Actualizar" );
            pId = bundle.getString( "pId" );
            pTitle = bundle.getString( "pTitle" );
            pDescription = bundle.getString( "pDescription" );

            mTitleEt.setText( pTitle );
            mDescriptionEt.setText( pDescription );
        }
        else {
            actionBar.setTitle( "Agregar Libro" );
            mSaveBtn.setText( "Guardar" );
        }
        //Progreso del dialogo
        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        mSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    //actualizar
                    String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    updateData(id, title, description );
                }
                else {
                    //agregar
                    //ingresar los datos
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    //Funcion para subir los datos
                    uploadData(title,description);

                }

            }
        } );

        //boton para llamar el listar
        mListBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        } );
    }

    private void updateData(String id, String title, String description) {
        //Titulo de progreso a
        pd.setTitle("Actualizando");
        //mostrar el progreso cuando se haga click en guardar
        pd.show();

        db.collection( "Documents" ).document(id)
                .update( "title", title, "description", description )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText( MainActivity.this, "Actualizado", Toast.LENGTH_SHORT ).show();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                        Toast.makeText( MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void uploadData(String title, String description) {
        //Titulo de progreso a
        pd.setTitle("Guardando");
        //mostrar el progreso cuando se haga click en guardar
        pd.show();
        //id aleatorio para cuando se guarde la info
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);//identificaion de la info
        doc.put("title", title);
        doc.put("description", description);

        //Agregar a la base de datos
        db.collection( "Documents" ).document(id).set( doc )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    //Informar correcto guardado

                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"Guardado", Toast.LENGTH_SHORT).show();
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //informar mal guardado
                        pd.dismiss();
                        //obtener y mostrar error
                        Toast.makeText( MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );
    }
}