package org.inspira.jcapiz.algolindo.otherfragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.database.SQLServerConnection;
import org.inspira.jcapiz.algolindo.main.R;
import org.inspira.jcapiz.algolindo.simpledata.MyListAdapter;

public class TableListFragment extends Fragment {


    private String url;
    private String user;
    private String password;
    private final java.util.ArrayList<String> tables = new java.util.ArrayList<>();
    private MyListAdapter tableListAdapter;
    private RecyclerView tablesContainer;

    private int entriesCount = 0;
    private int prevIndex = 0;
    private final int batchForEntries = 100;

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
        }
        tablesContainer = view.findViewById(R.id.tableViewContainer);
        tablesContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        tableListAdapter = new MyListAdapter(tables, getParentFragmentManager(), url, user, password);
        tablesContainer.setAdapter(tableListAdapter);
        connect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    private void addItemToTablesAdapter(String entry){
        tables.add(entry);
        entriesCount++;
        Activity activity = getActivity();
        if(entriesCount % batchForEntries == 0 && activity != null)
            activity.runOnUiThread(()->{
                tableListAdapter.notifyItemRangeInserted(prevIndex, batchForEntries);
                prevIndex = entriesCount;
                System.out.println("Popó " + entriesCount);
            });
    }

    private void toastIt(String text){
        Activity activity = getActivity();
        if(activity != null)
            getActivity().runOnUiThread(()-> Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show() );
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        int i = tables.size() - 1;
        tablesContainer.scrollToPosition(0);
        while(!tables.isEmpty()) {
            tables.remove(i);
            tableListAdapter.notifyItemRemoved(i);
            i--;
            entriesCount--;
        }
    }

    private void connect(){
        int i = tables.size() - 1;
        tablesContainer.scrollToPosition(0);
        while(!tables.isEmpty()) {
            tables.remove(i);
            tableListAdapter.notifyItemRemoved(i);
            i--;
            entriesCount--;
        }
        new Thread(){
            @Override
            public void run(){
                Activity activity = getActivity();
                toastIt("Connecting...");
                try (java.sql.Connection con = java.sql.DriverManager.getConnection(url, user, password)) {
                    try (java.sql.ResultSet rs = con.getMetaData().getTables(null, "dbo", null, null)) {
                        while (rs.next()) {
                            addItemToTablesAdapter(rs.getString(1) + "." + rs.getString(2) + "." + rs.getString(3));
                        }
                        toastIt("El EPA");
                        if(entriesCount % batchForEntries != 0 && activity != null)
                            activity.runOnUiThread(()->{
                                tableListAdapter.notifyItemRangeInserted(prevIndex, entriesCount - prevIndex);
                                prevIndex = entriesCount;
                            });
                        /*
                        toastIt("Now selecting some data...");
                        try (java.sql.PreparedStatement pstmnt = con.prepareStatement("select * from \"100000_demo_records_short\"")) {
                            try (java.sql.ResultSet rs2 = pstmnt.executeQuery()) {
                                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                                    System.out.print((i == 1 ? "" : ";") + rs2.getMetaData().getColumnName(i));
                                }
                                System.out.println();
                                int a = 0;
                                while (rs2.next()) {
                                    for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                                        System.out.print((i == 1 ? "" : ";") + rs2.getString(i));
                                    }
                                    System.out.println();
                                    a++;
                                    if (a == 100) {
                                        break;
                                    }
                                }
                            }
                        } catch (java.sql.SQLException e) {
                            toastIt("Got an error: " + e.getMessage());
                        }
                        */
                    }
                } catch (java.sql.SQLException e) {
                    toastIt("Got an error cn: " + e.getMessage());
                }
                toastIt("Terminating...");
            }
        }.start();
    }

}
