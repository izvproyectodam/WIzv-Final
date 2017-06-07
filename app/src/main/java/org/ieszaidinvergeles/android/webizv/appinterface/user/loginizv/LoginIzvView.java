package org.ieszaidinvergeles.android.webizv.appinterface.user.loginizv;

// WIzv

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.ieszaidinvergeles.android.webizv.R;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_REMEMBER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PASSWORD;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;

public class LoginIzvView extends AppCompatActivity {

    private Bundle bundle = null;

    private void back() {
        final Intent i = new Intent();
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_izv);
        bundle = getIntent().getExtras();

        final CheckBox cb = (CheckBox) findViewById(R.id.cbRemember);
        final EditText etSeneca = (EditText) findViewById(R.id.etUser);
        final EditText etClave = (EditText) findViewById(R.id.etPassword);

        final Button btAnonimo = (Button) findViewById(R.id.btAnonymous);
        btAnonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        final Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String clave = etClave.getText().toString();
                final String seneca = etSeneca.getText().toString();
                if(seneca.compareTo("") == 0 || clave.compareTo("") == 0) {
                    Snackbar.make(btLogin, R.string.error_acceso, Snackbar.LENGTH_LONG)
                            .setAction(R.string.error, null).show();
                    return;
                }
                bundle.putString(PASSWORD, clave);
                bundle.putBoolean(PARAMETER_REMEMBER, cb.isChecked());
                bundle.putString(USER, seneca);
                back();
            }
        });
    }

}