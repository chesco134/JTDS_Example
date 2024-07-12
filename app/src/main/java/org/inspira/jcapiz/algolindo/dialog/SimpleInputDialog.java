package org.inspira.jcapiz.algolindo.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.inspira.jcapiz.algolindo.main.R;

public class SimpleInputDialog extends DialogFragment {

    private String miNietaaaa;
    private String initText;
    private boolean asPassword;

    public static SimpleInputDialog makeDialog(String miNietaaaa, boolean asPassword, String initText){
        SimpleInputDialog sid = new SimpleInputDialog();
        Bundle bundle = new Bundle();
        bundle.putString("titulo_del_dialogo", miNietaaaa);
        bundle.putBoolean("as_password", asPassword);
        bundle.putString("texto_inicial", initText);
        sid.setArguments(bundle);
        return sid;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            miNietaaaa = args.getString("titulo_del_dialogo");
            asPassword = args.getBoolean("as_password");
            initText = args.getString("texto_inicial");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.simple_input_dialog, container, false);
        EditText inputText = layout.findViewById(R.id.editTextText);
        if(initText != null && !initText.isEmpty())
            inputText.setText(initText);
        if(asPassword)
            inputText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        Button cancelar = layout.findViewById(R.id.button2);
        Button aceptar = layout.findViewById(R.id.button);
        cancelar.setOnClickListener((view)-> dismiss() );
        aceptar.setOnClickListener((view) -> {
            Bundle costal = new Bundle();
            costal.putString(miNietaaaa, inputText.getText().toString());
            getParentFragmentManager().setFragmentResult("InputDialog " + miNietaaaa, costal);
            dismiss();
        });
        return layout;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(miNietaaaa);
        TextView tv = getDialog().findViewById(R.id.textView);
        tv.setText(miNietaaaa);
    }
    // Después de firmar contrato "de planta" se deciden cosas, tales como si se quiere ahorrar y disponer, complemento a la jubilacion
}
