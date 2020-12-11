package com.example.mybook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private static ClickListener mClickListener;
    TextView mTitleTv, mDescriptionTv;
    View mView;


    public ViewHolder(@NonNull View itemView) {
        super( itemView );

        mView = itemView;

        //item click
        itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        } );
        itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        } );

        //inicializar la vista con el layout_model
        mTitleTv = itemView.findViewById( R.id.rTitleTv );
        mDescriptionTv = itemView.findViewById( R.id.rDescriptionTv );
    }
        private ViewHolder.ClickListener mclickListener;
    //interfaz del llamaddo del click
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public static void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
