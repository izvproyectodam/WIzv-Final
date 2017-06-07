package org.ieszaidinvergeles.android.webizv.appinterface.user.web;

//WIzv

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver.RegistrationRequest;
import org.ieszaidinvergeles.android.webizv.appinterface.server.izvserver.SenecaRequest;
import org.ieszaidinvergeles.android.webizv.model.database.contract.DatabaseContract;
import org.ieszaidinvergeles.android.webizv.util.Preferences;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PASSWORD;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_TOKENSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_USERSTATE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.STATEVALUE_NEW;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.TOKEN;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;

public class WebPresenter implements WebContract.ActionsListener {

    final private Preferences preferences;
    final private WebContract.View view;

    public WebPresenter(WebContract.View view) {
        this.view = view;
        preferences = new Preferences(((Fragment)view).getActivity());
    }

    @Override
    public void closeSession() {
        preferences.unSet(PASSWORD);
        preferences.unSet(PREFERENCE_USERSTATE);
        preferences.unSet(USER);
    }

    @Override
    public void deleteNotifications() {
        final AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ((Fragment)view).getActivity().getContentResolver().delete(DatabaseContract.TABLE_NOTIFICATION_URI, "1 = 1", null);
                return null;
            }
        };
        task.execute();
    }

    @Override
    public String getPassword() {
        return preferences.get(PASSWORD);
    }

    @Override
    public String getUser() {
        return preferences.get(USER);
    }

    @Override
    public void registerToken() {
        final String tokenState = preferences.get(PREFERENCE_TOKENSTATE);
        final String token = preferences.get(TOKEN);
        if(token.compareTo("") != 0 && tokenState.compareTo(STATEVALUE_NEW) == 0) {
            final RegistrationRequest registrationRequest = new RegistrationRequest(((Fragment)view).getActivity());
            registrationRequest.send(token);
        }
    }

    @Override
    public void registerUser(String seneca) {
        final String senecaState = preferences.get(PREFERENCE_USERSTATE);
        final String tokenState = preferences.get(PREFERENCE_TOKENSTATE);
        if(senecaState.compareTo(STATEVALUE_NEW) == 0 || tokenState.compareTo(STATEVALUE_NEW) == 0) {
            final SenecaRequest senecaRequest = new SenecaRequest(((Fragment)view).getActivity());
            senecaRequest.send(seneca);
        }
    }

    @Override
    public void saveUser(String seneca, String password) {
        preferences.set(PASSWORD, password);
        preferences.set(USER, seneca);
        preferences.set(PREFERENCE_USERSTATE, STATEVALUE_NEW);
    }

}