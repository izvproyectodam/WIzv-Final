package org.ieszaidinvergeles.android.webizv.util;

// WIzv

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_SECRETKEY;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_SECRETMESSAGE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PREFERENCE_URLSERVER;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.SECRETKEY;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.SECRETMESSAGE;
import static org.ieszaidinvergeles.android.webizv.model.data.Constants.URLSERVER;

public class ServerConnection {

    private final Context context;
    private final ServerConnectionResponseListener listener;
    private final String data, page;
    private static String secretKey, secretMessage;

    public interface ServerConnectionResponseListener {
        void onServerResponse(String response);
    }

    public ServerConnection(Context context, String page, String data, ServerConnectionResponseListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
        this.page = page;
    }

    public boolean send() {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    final Preferences preferences = new Preferences(context);
                    final String server = preferences.get(PREFERENCE_URLSERVER, URLSERVER);
                    ServerConnection.secretKey = preferences.get(PREFERENCE_SECRETKEY, SECRETKEY);
                    ServerConnection.secretMessage = preferences.get(PREFERENCE_SECRETMESSAGE, SECRETMESSAGE);
                    return ServerConnection.request(server + page, data);
                }
                @Override
                protected void onPostExecute(String s) {
                    System.out.println(s);
                    listener.onServerResponse(s);
                }
            };
            task.execute();
            return true;
        }
        return false;
    }

    private static String request(String strUrl, String json) {
        InputStream is = null;
        String r = null;
        try {
            System.out.println(strUrl);
            System.out.println(json);
            final URL url = new URL(strUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Authorization", "Bearer " + Util.getJwt(ServerConnection.secretMessage, ServerConnection.secretKey));
            conn.setDoInput(true);
            conn.connect();
            final BufferedWriter out =  new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write(json);
            out.flush();
            out.close();
            is = conn.getInputStream();
            r = ServerConnection.readInputStream(is);
        } catch (UnsupportedEncodingException e) {
        } catch (ProtocolException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return r;
    }

    private static String readInputStream(InputStream stream) throws IOException {
        final int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        final Reader reader = new InputStreamReader(stream, "UTF-8");
        final StringBuilder sb = new StringBuilder();
        for (;;) {
            int bytes = reader.read(buffer, 0, buffer.length);
            if (bytes < 0) {
                break;
            }
            sb.append(buffer, 0, bytes);
        }
        return sb.toString();
    }
}