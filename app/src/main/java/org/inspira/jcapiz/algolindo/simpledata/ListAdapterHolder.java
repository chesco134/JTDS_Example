package org.inspira.jcapiz.algolindo.simpledata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.main.R;

public class ListAdapterHolder extends RecyclerView.ViewHolder {

    public TextView leche;

    public ListAdapterHolder(@NonNull View itemView) {
        super(itemView);
        leche = itemView.findViewById(R.id.leche);
    }
}
