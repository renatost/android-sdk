package saferize.com.sdk;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.saferize.sdk.Approval;
import com.saferize.sdk.AuthenticationException;
import com.saferize.sdk.Configuration;
import com.saferize.sdk.SaferizeClient;
import com.saferize.sdk.SaferizeClientException;
import com.saferize.sdk.SaferizeSession;
import com.saferize.sdk.WebsocketException;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class SaferizeService extends Service {



    protected static final String RESULT_PARAM = "result";

    protected static final int EXCEPTION_CALLABACK_CODE = -1;
    protected static final int SIGNED_UP_CALLBACK_CODE = 1;
    protected static final int CONNECT_CALLBACK_CODE = 2;
    protected static final int DISCONNECT_CALLBACK_CODE = 3;
    protected static final int ERROR_CALLBACK_CODE = 4;
    protected static final int PAUSE_CALLBACK_CODE = 5;
    protected static final int RESUME_CALLBACK_CODE = 6;
    protected static final int TIME_UP_CALLBACK_CODE = 7;
    protected static final int SESSION_CREATED_CALLBACK_CODE = 8;

    private SaferizeClient client;






    private ExecutorService pool = Executors.newSingleThreadExecutor();

    private final LocalBinder binder = new LocalBinder();

    public SaferizeService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public class LocalBinder extends Binder {
        public SaferizeService getService() {
            return SaferizeService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void init(Configuration configuration) throws WebsocketException, AuthenticationException {
        client = new SaferizeClient (configuration);
    }




    public void signUp(String parentEmail, String userToken, SaferizeCallback callback) {
        pool.submit(() -> {
            try {
                Bundle bundle = new Bundle();
                Approval approval = client.signUp(parentEmail, userToken);
                bundle.putSerializable(RESULT_PARAM, approval);
                callback.send(SIGNED_UP_CALLBACK_CODE, bundle);
            } catch (SaferizeClientException e) {
                sendError(callback, e);
            }

        });

    }

    public void createSession(String userToken, SaferizeCallback callback) {
        pool.submit(() -> {
            try {
                SaferizeSession session = client.createSession(userToken);
                sendResult(SESSION_CREATED_CALLBACK_CODE, callback, session);
            } catch (SaferizeClientException e) {
                sendError(callback, e);
            }
        });
    }

    public void startWebSocket(SaferizeCallback callback) {
        pool.submit(() -> {
            try {
                client.onConnect( session ->  sendResult(CONNECT_CALLBACK_CODE, callback, session) );
                client.onDisconnect(session -> sendResult(DISCONNECT_CALLBACK_CODE, callback, session));
                client.onError(session -> sendResult(ERROR_CALLBACK_CODE, callback, session));
                client.onPause(session -> sendResult(PAUSE_CALLBACK_CODE, callback, session));
                client.onResume(session -> sendResult(RESUME_CALLBACK_CODE, callback, session));
                client.onTimeIsUp(session -> sendResult(TIME_UP_CALLBACK_CODE, callback, session));
                client.startWebsocketConnection();
            } catch (WebsocketException e) {
                sendError(callback, e);
            }
        });
    }



    private void sendResult(int code, ResultReceiver receiver, SaferizeSession session) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESULT_PARAM, session);
        receiver.send(code, bundle);

    }
    private void sendError(ResultReceiver receiver, Exception error) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESULT_PARAM, error);
        receiver.send(EXCEPTION_CALLABACK_CODE, bundle);
    }




}
