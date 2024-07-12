package org.inspira.jcapiz.algolindo.simpledata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.main.R;
import org.inspira.jcapiz.algolindo.otherfragments.TableDataViewFragment;

public class MyListAdapter extends RecyclerView.Adapter<ListAdapterHolder>{

    private final FragmentManager fragmentManager;
    private final String url;
    private final String user;
    private final String password;
    private final java.util.ArrayList<String> list;

    public MyListAdapter(java.util.ArrayList<String> list, FragmentManager fragmentManager, String url, String user, String password){
        this.list = list;
        this.fragmentManager = fragmentManager;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_entry, parent, false);
        view.setOnClickListener((vw)->{
            FragmentTransaction tr = fragmentManager.beginTransaction();
            Fragment f = fragmentManager.findFragmentByTag("Table Data");
            if(f != null){
                tr.detach(f);
            }
            TableDataViewFragment tlf = new TableDataViewFragment();
            Bundle args = new Bundle();
            args.putString("url", url);
            args.putString("user", user);
            args.putString("password", password);
            args.putString("table_name", ((TextView)vw.findViewById(R.id.leche)).getText().toString());
            tlf.setArguments(args);
            tr.replace(R.id.fragmentContainerView, tlf, "Table Data");
            tr.commit();
        });
        return new ListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder holder, int position) {
        holder.leche.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
