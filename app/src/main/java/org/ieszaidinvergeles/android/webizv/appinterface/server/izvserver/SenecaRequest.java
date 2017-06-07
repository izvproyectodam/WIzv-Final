package org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver;

// WIzv

import android.content.Context;

import org.ieszaidinvergeles.android.webizv.model.pojo.Token;
import org.ieszaidinvergeles.android.webizv.model.util.PojoUtil;
import org.ieszaidinvergeles.android.webizv.util.Preferences;
import org.ieszaidinvergeles.android.webizv.util.ServerConnection;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.TOKEN;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_TOKENSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_USERSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_NEW;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_SEND;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLSERVERTOKEN;

public class SenecaRequest implements ServerConnection.ServerConnectionResponseListener {

    final private Context context;
    final private Preferences preferences;

    public SenecaRequest(Context context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    @Override
    public void onServerResponse(String response) {
        final Long r = PojoUtil.getResponse(response);
        if(r != null && r > 0) {
            preferences.set(PREFERENCE_USERSTATE, STATEVALUE_SEND);
            preferences.set(PREFERENCE_TOKENSTATE, STATEVALUE_SEND);
        }
    }

    public boolean send(String seneca) {
        preferences.set(PREFERENCE_USERSTATE, STATEVALUE_NEW);
        final String token = preferences.get(TOKEN);
        if(token.compareTo("") != 0) {
            final Token tokenObject = new Token(seneca, token);
            final ServerConnection connection = new ServerConnection(context, URLSERVERTOKEN, PojoUtil.toJson(tokenObject), this);
            return connection.send();
        }
        return false;
    }
}