package org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver;

// WIzv

import android.content.Context;

import org.ieszaidinvergeles.android.webizv.model.pojo.Token;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.util.Preferences;
import org.ieszaidinvergeles.android.webizv.util.ServerConnection;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_TOKENSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_USERSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_NEW;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_SEND;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLSERVERTOKEN;

public class RegistrationRequest implements ServerConnection.ServerConnectionResponseListener {

    final private Context context;
    final private Preferences preferences;

    public RegistrationRequest(Context context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    @Override
    public void onServerResponse(String response) {
        final Long r = PojoUtil.getResponse(response);
        if(r != null && r > 0) {
            preferences.set(PREFERENCE_TOKENSTATE, STATEVALUE_SEND);
        }
    }

    public boolean send(String token) {
        preferences.set(PREFERENCE_TOKENSTATE, STATEVALUE_NEW);
        final String seneca = preferences.get(USER);
        final String senecaState = preferences.get(PREFERENCE_USERSTATE);
        Token tokenObject;
        if(seneca.compareTo("") != 0 && senecaState.compareTo(STATEVALUE_SEND) == 0) {
            tokenObject = new Token(seneca, token);
        } else {
            tokenObject = new Token(token);
        }
        final ServerConnection connection = new ServerConnection(context, URLSERVERTOKEN, PojoUtil.toJson(tokenObject), this);
        return connection.send();
    }
}