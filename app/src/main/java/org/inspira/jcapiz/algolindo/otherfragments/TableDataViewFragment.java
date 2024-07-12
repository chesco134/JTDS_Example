package org.inspira.jcapiz.algolindo.otherfragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.database.SQLServerConnection;
import org.inspira.jcapiz.algolindo.main.R;
import org.inspira.jcapiz.algolindo.simpledata.MySimpleDataViewAdapter;

public class TableDataViewFragment extends Fragment {

    private String url;
    private String user;
    private String password;
    private String tableName;

    private RecyclerView tablesContainer;
    private MySimpleDataViewAdapter adapter;

    private java.util.ArrayList<String> entries = new java.util.ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState){
        return inflater.inflate(R.layout.table_list_fragment, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            url = args.getString("url");
            user = args.getString("user");
            password = args.getString("password");
            tableName = args.getString("table_name");
            new Thread(()-> {
                Activity activity = getActivity();
                SQLServerConnection con = new SQLServerConnection(url, user, password);
                if(activity != null){
                    activity.runOnUiThread(()-> Toast.makeText(activity, "RUNNING a query on table: " + tableName, Toast.LENGTH_LONG).show());
                }
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("([^.]+)$").matcher(tableName);
                if(m.find()){
                    String quote = con.getQuote();
                    con.prepare("select * from " + quote + m.group() + quote);
                    con.executeQuery();
                    java.util.ArrayList<String> headers = con.getHeader();
                    entries.addAll(headers);
                    java.util.ArrayList<Object> objectList = null;
                    Object o;
                    int cnt = 0;
                    while (!(objectList = con.getObjects()).isEmpty()) {
                        for (int i = 0; i < objectList.size(); i++) {
                            o = objectList.get(i);
                            entries.add(o == null ? "" : String.valueOf(o));
                        }
                        cnt++;
                        if (cnt == 1000)
                            break;
                    }
                    if(activity != null) {
                        activity.runOnUiThread(() -> {
                            tablesContainer = view.findViewById(R.id.tableViewContainer);
                            tablesContainer.setLayoutManager(new GridLayoutManager(getContext(), headers.size()));
                            adapter = new MySimpleDataViewAdapter(entries);
                            tablesContainer.setAdapter(adapter);
                        });
                    }
                }
            }).start();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

}
