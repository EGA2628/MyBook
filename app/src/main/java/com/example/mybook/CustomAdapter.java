package com.example.mybook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ListActivity listActivity;
    List<Model> modelList;
    Context context;

    public CustomAdapter(ListActivity listActivity, List<Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate( R.layout.model_layout, viewGroup, false );
        ViewHolder viewHolder = new ViewHolder(itemView);

        ViewHolder.setOnClickListener( new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String title = modelList.get( position ).getTitle();
                String description = modelList.get( position ).getDescription();
                Toast.makeText( listActivity, title+"\n"+description, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder( listActivity );
                //opciones de pantallas en dialogo
                String[] options = {"Actualizar", "Eliminar"};
                builder.setItems( options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //Actualizar
                            String id = modelList.get( position ).getId();
                            String Title = modelList.get( position ).getTitle();
                            String description = modelList.get( position ).getDescription();

                            Intent intent = new Intent(listActivity, MainActivity.class);

                            intent.putExtra( "pId", id );
                            intent.putExtra( "pTitle", Title );
                            intent.putExtra( "pDescription", description );

                            listActivity.startActivity( intent );
                        }
                        if (which == 1) {
                            //Eliminar
                            listActivity.deleteData( position );
                        }
                    }
                } ).create().show();
            }
        } );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mTitleTv.setText(modelList.get( i ).getTitle());
        viewHolder.mDescriptionTv.setText(modelList.get( i ).getDescription());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
