package org.inspira.jcapiz.algolindo.simpledata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.main.R;

public class MySimpleDataViewAdapter extends RecyclerView.Adapter<ListAdapterHolder> {

    private java.util.ArrayList<String> rows;

    public MySimpleDataViewAdapter(java.util.ArrayList<String> rows){
        this.rows = rows;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_entry, parent, false);
        return new ListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {
        holder.leche.setText(rows.get(position));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public void setRows(java.util.ArrayList<String> rows){
        this.rows = rows;
    }
}
