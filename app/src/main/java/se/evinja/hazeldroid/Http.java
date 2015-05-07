package se.evinja.hazeldroid;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Http {
    private HttpClient httpClient = new DefaultHttpClient();
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String BASE_PATH = "http://178.62.213.135:3000/";
    private Http_Events eventListener;
    private final String username, password;

    public Http (Http_Events eventListener, String username, String password){
        this.eventListener = eventListener;
        this.username = username;
        this.password = password;
    }

    public void GET(final String server_path){

        class Http_Getter extends AsyncTask<String,Void,String>{
            String backgroundErrors = "";
            @Override
            protected String doInBackground(String... params) {
                try {
                    HttpGet httpGet = new HttpGet(params[0]);
                    httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(params[1], params[2]), "UTF-8", false));
                    String result = "";

                    HttpResponse response = httpClient.execute(httpGet);
                    InputStream is = response.getEntity().getContent();

                    if (is != null){
                        result = convertStreamToString(is);
                        return result;

                    }else{
                        Log.i("###### GET GOT NULL", "");
                    }


                } catch (Exception e) {
                    backgroundErrors += e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (!backgroundErrors.isEmpty()){
                    eventListener.onError("Error in GET: " + backgroundErrors);
                }
                eventListener.onData(result);
            }

        }
        Http_Getter get = new Http_Getter(); // Must be after the class
        get.execute(BASE_PATH + username + "/" +  server_path, username, password); //TODO ENCODE

    }

    public void PUT(String server_path, final JSONObject jsonData){

        class Http_poster extends AsyncTask<Void, Void, String> {
            JSONObject json;
            String serverPath;
            String doInBackgroundErrors = ""; //Errors are saved until they can be handled - cant handle error in background thread
            String spath;
            String postUser;
            String postPwd;
            public Http_poster(final String postUrl,  final String user, final String password,  final JSONObject postJSON) {
                json = postJSON;
                serverPath = user + "/" + postUrl;
                postUser = user;
                postPwd = password;

            }

            @Override
            protected String doInBackground(Void... params){
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpClient client = new DefaultHttpClient(httpParams);
                spath = (BASE_PATH +  serverPath).replaceAll(" ", "%20");
                HttpPut request = new HttpPut(spath);
                request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(postUser, postPwd), "UTF-8", false));
                request.setHeader( "Content-Type", "application/json" );

                try {
                    StringEntity se = new StringEntity(json.toString(),"UTF-8");
                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    request.setEntity(se);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    String strResponse = convertStreamToString(is); //Convert stream to string
                    return strResponse;

                } catch (Exception e) {
                    doInBackgroundErrors += e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    if (!doInBackgroundErrors.isEmpty()) { //Error occured in background - now we can handle it
                        eventListener.onError("POST Background: " + doInBackgroundErrors);
                        doInBackgroundErrors = "";
                        return;
                    }
                    if (result == null){
                        Log.i("###### PUT GOT NULL", "");
                        return; //Should not come here. Maybe.
                    }
                    try {
                        Log.i("###### PUT GOT DATA", result);
                        eventListener.onData(result);

                    } catch (Exception e) {
                        eventListener.onError("Bad PUT JSON-data received: " + result);
                    }
                }catch (Exception e){
                    eventListener.onError("onPostExecute exception, data:" + result + ". Message" + e.getMessage());
                }
            }

        }
        Http_poster sendPostReqAsyncTask = new Http_poster(server_path, username, password, jsonData);
        sendPostReqAsyncTask.execute();
    }

    public void DELETE(String server_path){

        class Http_poster extends AsyncTask<Void, Void, String> {
            JSONObject json;
            String serverPath;
            String doInBackgroundErrors = ""; //Errors are saved until they can be handled - cant handle error in background thread
            String spath;
            String postUser;
            String postPwd;
            public Http_poster(final String postUrl,  final String user, final String password) {
                serverPath = user + "/" + postUrl;
                postUser = user;
                postPwd = password;

            }

            @Override
            protected String doInBackground(Void... params){
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpClient client = new DefaultHttpClient(httpParams);
//                spath = Uri.encode(BASE_PATH +  serverPath);
                spath = (BASE_PATH +  serverPath).replaceAll(" ", "%20");
                HttpDelete request = new HttpDelete(spath);
                request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(postUser, postPwd), "UTF-8", false));
                request.setHeader( "Content-Type", "application/json" );

                try {
//                    StringEntity se = new StringEntity(json.toString(),"UTF-8");
//                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                    request.setEntity(se);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    String strResponse = convertStreamToString(is); //Convert stream to string
                    return strResponse;

                } catch (Exception e) {
                    doInBackgroundErrors += e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    if (!doInBackgroundErrors.isEmpty()) { //Error occurred in background - now we can handle it
                        eventListener.onError("DELETE Background: " + doInBackgroundErrors);
                        doInBackgroundErrors = "";
                        return;
                    }
                    if (result == null){
                        Log.i("###### DELETE GOT NULL", "");
                        return; //Should not come here. Maybe.
                    }
                    try {
                        Log.i("###### DELETE GOT DATA", result);
                        eventListener.onData(result);

                    } catch (Exception e) {
                        eventListener.onError("Bad DELETE JSON-data received: " + result);
                    }
                }catch (Exception e){
                    eventListener.onError("onPostExecute exception, data:" + result + ". Message" + e.getMessage());
                }
            }

        }
        Http_poster sendPostReqAsyncTask = new Http_poster(server_path, username, password);
        sendPostReqAsyncTask.execute();
    }

    public void POST(String server_path, final JSONObject jsonData){

        class Http_poster extends AsyncTask<Void, Void, String> {
            JSONObject json;
            String serverPath;
            String doInBackgroundErrors = ""; //Errors are saved until they can be handled - cant handle error in background thread
            String spath;
            String postUser;
            String postPwd;
            public Http_poster(final String postUrl,  final String user, final String password,  final JSONObject postJSON) {
                json = postJSON;
                serverPath = user + "/" + postUrl;
                postUser = user;
                postPwd = password;

            }

            @Override
            protected String doInBackground(Void... params){
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
                HttpClient client = new DefaultHttpClient(httpParams);
//                spath = Uri.encode(BASE_PATH +  serverPath); //TODO

                HttpPost request = new HttpPost(BASE_PATH +  serverPath);
                request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(postUser, postPwd), "UTF-8", false));
                request.setHeader( "Content-Type", "application/json" );

                try {
                    StringEntity se = new StringEntity(json.toString(),"UTF-8");
                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    request.setEntity(se);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    String strResponse = convertStreamToString(is); //Convert stream to string
                    return strResponse;

                } catch (Exception e) {
                    doInBackgroundErrors += e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    if (!doInBackgroundErrors.isEmpty()) { //Error occured in background - now we can handle it
                        eventListener.onError("POST Background: " + doInBackgroundErrors);
                        doInBackgroundErrors = "";
                        return;
                    }
                    if (result == null){
                        Log.i("###### POST GOT NULL", "");
                        return; //Should not come here. Maybe.
                    }
                    try {
                        Log.i("###### POST GOT DATA", result);
                        eventListener.onData(result);

                    } catch (Exception e) {
                        eventListener.onError("Bad POST JSON-data received: " + result);
                    }
                }catch (Exception e){
                    eventListener.onError("onPostExecute exception, data:" + result + ". Message" + e.getMessage());
                }
            }

        }
        Http_poster sendPostReqAsyncTask = new Http_poster(server_path, username, password, jsonData);
        sendPostReqAsyncTask.execute();
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            eventListener.onError("convertStreamToString: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
