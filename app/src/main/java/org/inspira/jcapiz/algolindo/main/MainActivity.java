package org.inspira.jcapiz.algolindo.main;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.inspira.jcapiz.algolindo.dialog.SimpleInputDialog;
import org.inspira.jcapiz.algolindo.otherfragments.TableListFragment;
import org.inspira.jcapiz.algolindo.simpledata.MyListAdapter;

public class MainActivity extends AppCompatActivity {

    private boolean connecting = false;
    private boolean jtdsClass = false;

    private String url;
    private String user;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!jtdsClass)
            try{
                jtdsClass = true;
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
            }catch(ClassNotFoundException ignore){
            }
        final SharedPreferences sp = getSharedPreferences("Ajonjolí", MODE_PRIVATE);
        url = sp.getString("url", getResources().getString(R.string.empty_url));
        user = sp.getString("user", getResources().getString(R.string.empty_user));
        password = sp.getString("password", getResources().getString(R.string.empty_password));
        TextView dbUrl = findViewById(R.id.dbURL);
        dbUrl.setOnClickListener((view)->{
            final String tit = "SQL Server URL";
            showDialog(tit, url, (requestKey, result)->{
                url = result.getString(tit);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("url", url);
                editor.apply();
                if(url != null && !url.isEmpty())
                    dbUrl.setText(url);
            });
        });
        TextView dbUser = findViewById(R.id.dbUser);
        dbUser.setOnClickListener( (view)-> {
            final String tit = "SQL Server User Name";
            showDialog(tit, user, (requestKey, result)->{
                user = result.getString(tit);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user", user);
                editor.apply();
                if(user != null && !user.isEmpty())
                    dbUser.setText(user);
            });
        });
        TextView dbPsswd = findViewById(R.id.dbPassword);
        dbPsswd.setOnClickListener((view)-> {
            final String tit = "SQL Server User Password";
            showDialog(tit, true, (requestKey, result)->{
                password = result.getString(tit);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("password", password);
                editor.apply();
                if(password != null && !password.isEmpty())
                    dbPsswd.setText(password);
            });
        });
        Button probarConexion = findViewById(R.id.button3);
        probarConexion.setOnClickListener((view)-> testConnectionButtonAction());
        dbUrl.setText(url);
        dbUser.setText(user);
        dbPsswd.setText(password);
    }

    private void testConnectionButtonAction(){
        if(!connecting) {
            connecting = true;
            FragmentManager mgr = getSupportFragmentManager();
            FragmentTransaction tr = mgr.beginTransaction();
            Fragment f = mgr.findFragmentByTag("Table List");
            if(f != null){
                tr.detach(f);
            }
            TableListFragment tlf = new TableListFragment();
            Bundle args = new Bundle();
            args.putString("url", url);
            args.putString("user", user);
            args.putString("password", password);
            tlf.setArguments(args);
            tr.replace(R.id.fragmentContainerView, tlf, "Table List");
            tr.commit();
            connecting = false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menucito, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if (id == R.id.sqlserver_host) {
            showDialog("SQL Server URL", (requestKey, result)-> url = result.getString("url"));
            return true;
        } else if(id == R.id.sqlserver_psswd){
            showDialog("SQL Server User Password", (requestKey, result)-> password = result.getString("password") );
            return true;
        } else if(id == R.id.sqlserver_user){
            showDialog("SQL Server User Name", (requestKey, result)-> user = result.getString("user") );
            return true;
        } else {
            super.onOptionsItemSelected(menuItem);
        }
        return false;
    }

    private void showDialog(String title, boolean asPassword, String initText, FragmentResultListener frl){
        SimpleInputDialog sid = SimpleInputDialog.makeDialog(title, asPassword, initText);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = getSupportFragmentManager().findFragmentByTag("InputDialog");
        if(f != null){
            ft.remove(f);
        }
        ft.addToBackStack(null);
        getSupportFragmentManager().setFragmentResultListener("InputDialog " + title, sid, frl);
        sid.show(ft, "InputDialog");
    }

    private void showDialog(String title, FragmentResultListener frl){
        showDialog(title, false, null, frl);
    }

    private void showDialog(String title, boolean asPassword, FragmentResultListener frl){
        showDialog(title, asPassword, null, frl);
    }

    private void showDialog(String title, String initText, FragmentResultListener frl){
        showDialog(title, false, initText, frl);
    }
}