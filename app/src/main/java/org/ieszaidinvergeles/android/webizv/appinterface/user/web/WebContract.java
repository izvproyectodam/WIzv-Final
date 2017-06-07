package org.ieszaidinvergeles.android.webizv.appinterface.user.web;

//WIzv

public interface WebContract {

    interface ActionsListener {

        void closeSession();

        void deleteNotifications();

        String getPassword();

        String getUser();

        void registerToken();

        void registerUser(String seneca);

        void saveUser(String seneca, String password);

    }

    interface View {

        void showCloseSession();

        void showDeleteNotifications();

        void showExitApp();

        void showNotifications();

        void showOpenSession();

    }
}