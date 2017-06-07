package org.ieszaidinvergeles.android.webizv.appinterface.server.firebasecloud;

// WIzv

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver.RegistrationRequest;
import org.ieszaidinvergeles.android.webizv.util.Preferences;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.TOKEN;

public class TokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        final Preferences preferences = new Preferences(getApplicationContext());
        final RegistrationRequest registrationRequest = new RegistrationRequest(getApplicationContext());
        preferences.set(TOKEN, token);
        registrationRequest.send(token);
    }
}