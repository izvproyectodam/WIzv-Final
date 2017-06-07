package org.ieszaidinvergeles.android.webizv.appinterface.user.web;

//WIzv

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.appinterface.user.loginizv.LoginIzvView;
import org.ieszaidinvergeles.android.webizv.appinterface.user.notification.NotificationView;

import java.util.Timer;
import java.util.TimerTask;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_REMEMBER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_URL;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PASSWORD;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URL;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLLOGIN;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLLOGOUT;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLPROFES;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.USER;

public class WebFragment extends Fragment implements WebContract.View {

    private static final int ACTIVIDAD_LOGIN = 1;
    private static final int ACTIVIDAD_NOTIFICACION = 2;

    private boolean isLogged = false;
    private int accessCount = 0;
    private WebContract.ActionsListener listener;
    private String password = null, url = URL, user = null;
    private android.webkit.WebView wvIzv;

    public WebFragment() {
    }

    private void checkVisibility() {
        Timer timer;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                wvIzv.post(new Runnable() {
                    @Override
                    public void run() {
                        wvIzv.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        timer.schedule(task, 4000);
    }

    private void clearCookiesAndCache() {
        CookieSyncManager.createInstance(getActivity());
        final CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }
    }

    private void getInitialData() {
        password = listener.getPassword();
        url = URL;
        user = listener.getUser();
        if (user.compareTo("") != 0) {
            wvIzv.setVisibility(View.GONE);
            url = URLLOGIN;
        }
        wvIzv.loadUrl(url);
    }

    private void initWebView(View root) {
        wvIzv = (android.webkit.WebView) root.findViewById(R.id.wvIzv);

        wvIzv.getSettings().setDomStorageEnabled(true);
        wvIzv.getSettings().setJavaScriptEnabled(true);

        wvIzv.setFocusableInTouchMode(true);
        wvIzv.requestFocus();
        wvIzv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (wvIzv != null && wvIzv.canGoBack()) {
                            wvIzv.goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        wvIzv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.compareTo(URLLOGIN) == 0) {
                    final String js = "document.getElementById('USUARIO').value = '" + user + "';" +
                            "document.getElementById('CLAVE').value = '" + password + "';" +
                            "comprobarclave();" +
                            "var intervalo = setInterval(function() {" +
                            "    if(document.getElementById('MENSAJE').innerHTML == '<center>Usuario incorrecto</center>') {" +
                            "        location.replace('" + URLLOGOUT + "');" +
                            "        clearInterval(intervalo);" + //?
                            "    }" +
                            "}, 500);";
                    view.loadUrl("javascript: " + js);
                }
                if (accessCount > 1 && isLogged) {
                    wvIzv.setVisibility(View.VISIBLE);
                }
                if(wvIzv.getVisibility() == View.GONE) {
                    checkVisibility();
                }
            }

            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                accessCount++;
                if(accessCount == 2 && user.compareTo("") != 0) {
                    if (url.compareTo(URLPROFES) == 0 &&
                            WebFragment.this.url.compareTo(URLLOGIN) == 0) {
                        isLogged = true;
                        listener.registerUser(user);
                        getActivity().invalidateOptionsMenu();
                    } else {
                        showCloseSession();
                    }
                }
                if (url.compareTo(URLLOGOUT) == 0) {
                    showCloseSession();
                }
            }

            @Override
            public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                final AlertDialog.Builder alerta = new AlertDialog.Builder(WebFragment.this.getContext());
                alerta.setMessage(description).setPositiveButton(R.string.aceptar, null).setTitle(R.string.error);
                alerta.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                if (url.compareTo(URLLOGIN) == 0 && user.compareTo("") == 0) {
                    url = URL;
                }
                view.loadUrl(url);
                return true;
            }
        });

        final ProgressBar pbProgreso = (ProgressBar) root.findViewById(R.id.pbProgress);
        wvIzv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pbProgreso.setVisibility(View.VISIBLE);
                pbProgreso.setProgress(0);
                pbProgreso.incrementProgressBy(newProgress);
                if (newProgress == 100) {
                    pbProgreso.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean isLogged() {
        return isLogged;
    }

    public static WebFragment newInstance(Bundle arguments) {
        WebFragment wf = new WebFragment();
        wf.setArguments(arguments);
        return wf;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK &&
                (requestCode == ACTIVIDAD_NOTIFICACION ||
                 requestCode == ACTIVIDAD_LOGIN)) {
            final Bundle bundle = data.getExtras();
            url = bundle.getString(PARAMETER_URL);
            user = bundle.getString(USER);
            if(user != null) {
                final boolean remember = bundle.getBoolean(PARAMETER_REMEMBER);
                password = bundle.getString(PASSWORD);
                url = URLLOGIN;
                if(remember) {
                    listener.saveUser(user, password);
                }
                accessCount = 0;
                wvIzv.loadUrl(url);
                wvIzv.setVisibility(View.GONE);
            } else {
                user = "";
                password = "";
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new WebPresenter(this);
        listener.registerToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.web_fragment, container, false);
        initWebView(root);
        getInitialData();
        return root;
    }

    @Override
    public void showCloseSession() {
        isLogged = false;
        password = "";
        user = "";
        listener.saveUser(user, password);
        listener.closeSession();
        clearCookiesAndCache();
        getActivity().invalidateOptionsMenu();
        wvIzv.loadUrl(URL);
    }

    @Override
    public void showDeleteNotifications() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.seguro)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.deleteNotifications();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .setTitle(R.string.borrar_notificaciones);
        alert.show();
    }

    @Override
    public void showExitApp() {
        clearCookiesAndCache();
        getActivity().finish();
    }

    @Override
    public void showNotifications() {
        final Intent i = new Intent(getActivity(), NotificationView.class);
        i.putExtra(PARAMETER_URL, wvIzv.getUrl());
        startActivityForResult(i, ACTIVIDAD_NOTIFICACION);
    }

    @Override
    public void showOpenSession() {
        final Intent i = new Intent(getActivity(), LoginIzvView.class);
        i.putExtra(PARAMETER_URL, wvIzv.getUrl());
        startActivityForResult(i, ACTIVIDAD_LOGIN);
    }
}