package saferize.com.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.saferize.sdk.Approval;
import com.saferize.sdk.SaferizeSession;

import java.util.function.Consumer;

import static saferize.com.sdk.SaferizeService.*;

public class SaferizeCallback extends ResultReceiver {

    public interface Event<T> {
        void onEvent(T event);
    }


    private Event<Approval> onSignedUp;
    private Event<Exception> onException;
    private Event<SaferizeSession> onSessionCreated;
    private Event<SaferizeSession> onConnect;
    private Event<SaferizeSession> onDisconnect;
    private Event<SaferizeSession> onError;
    private Event<SaferizeSession> onPause;
    private Event<SaferizeSession> onResume;
    private Event<SaferizeSession> onTimeUp;




    public SaferizeCallback(Handler handler) {
        super(handler);


    }


    public void onSignedUp(Event<Approval> signedUp) {
        this.onSignedUp = signedUp;
    }

    public void onException(Event<Exception> exception) {
        this.onException = exception;
    }


    public void onSessionCreated(Event<SaferizeSession> onSessionCreated) {
        this.onSessionCreated = onSessionCreated;
    }

    public void onConnect(Event<SaferizeSession> onConnect) {
        this.onConnect = onConnect;
    }

    public void onDisconnect(Event<SaferizeSession> onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    public void onError(Event<SaferizeSession> onError) {
        this.onError = onError;
    }

    public void onPause(Event<SaferizeSession> onPause) {
        this.onPause = onPause;
    }
    public void onResume(Event<SaferizeSession> onResume) {
        this.onResume = onResume;
    }

    public void onTimeUp(Event<SaferizeSession> onTimeUp ) {
        this.onTimeUp = onTimeUp;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        switch (resultCode) {
            case SIGNED_UP_CALLBACK_CODE:
                Approval approval = (Approval) resultData.getSerializable(RESULT_PARAM);
                if (onSignedUp != null) {
                    onSignedUp.onEvent(approval);
                }
                break;
            case EXCEPTION_CALLABACK_CODE:
                if (onException != null) {
                    Exception e = (Exception) resultData.getSerializable(RESULT_PARAM);
                    onException.onEvent(e);
                }
                break;
            case SESSION_CREATED_CALLBACK_CODE:
                if (onSessionCreated != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onSessionCreated.onEvent(session);
                }
                break;
            case CONNECT_CALLBACK_CODE:
                if (onConnect != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onConnect.onEvent(session);
                }
                break;
            case DISCONNECT_CALLBACK_CODE:
                if (onDisconnect != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onDisconnect.onEvent(session);
                }
                break;
            case ERROR_CALLBACK_CODE:
                if (onError != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onError.onEvent(session);
                }
                break;
            case PAUSE_CALLBACK_CODE:
                if (onPause != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onPause.onEvent(session);
                }
                break;
            case RESUME_CALLBACK_CODE:
                if (onResume != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onResume.onEvent(session);
                }
                break;
            case TIME_UP_CALLBACK_CODE:
                if (onTimeUp != null) {
                    SaferizeSession session = (SaferizeSession) resultData.getSerializable(RESULT_PARAM);
                    onTimeUp.onEvent(session);
                }
        }
    }






}
