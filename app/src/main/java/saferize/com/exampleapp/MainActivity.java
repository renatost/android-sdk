package saferize.com.exampleapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;

import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.saferize.sdk.Approval;
import com.saferize.sdk.AuthenticationException;
import com.saferize.sdk.Configuration;
import com.saferize.sdk.SaferizeClient;
import com.saferize.sdk.SaferizeSession;
import com.saferize.sdk.WebsocketException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import saferize.com.sdk.SaferizeCallback;
import saferize.com.sdk.SaferizeService;


public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private Button signUpButton;
    private EditText parentEmailText;
    private TextView logTextView;
    private SaferizeCallback callback;
    private String userToken;
    private SaferizeService saferizeService;


    private static final String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEA06TiLHrxKaJp5E50cMEjqB4hd7wGGt/NwhuqAKM4F1881AlK\n" +
            "GmOqECxpNU/qLaIQcye5DXPzWusLqALB7ATLvlLz/c/62Vg8jUKfH17TGIbms0al\n" +
            "zsdTQb9I6pQLZQbxpuuiurGcfqsRZHWICI5/0wBBcmfnMKHZj6MVdrZTfifQHEJz\n" +
            "LEk6uQ4Du1iBFkoSQQVW/yOaoMPpyAf5AgbdyX3REf2n2Ijs3uS6Uhpc5AZFhMiz\n" +
            "ChPd3oR6+2VsyMbRvCK/A9uqrynw8s2AEWgLlW8PpFwUvvDw1fX+d+1Gsp+fuJud\n" +
            "cPpJKELFKwJyeSdrf6g2eGZFZjpSDJk9NzA2/QIDAQABAoIBAC3Vqom5glri6o3g\n" +
            "E8WLfl5dUCAvHx9Y0qWz+ggzUOV24aSF8n9ukBj6lTpPeUayr19Q/fmU3+ITvy1+\n" +
            "k2K60yj/rAmOriO9wTdSc0WG8q6AIJw4s5XpgvVdKLxsnV8ettzQcSh/aIXiJF0e\n" +
            "OvynZ7VZe9L7/4x/sK8zwWU5LTHHppW2JJQ6JStNTLIsbV5Bc+vFcnbVWI+k5PfA\n" +
            "NBUUICo/xDy45GY9i3acHQwq/tYXHItrzwgnlm5PG49kekQMOBcXWMZa+CuXZbpF\n" +
            "jz8lEQ5pXlN52eFvF12VJC3D+kBrH5ZGeYHVzWIuPGyQmgt49kB9QmiI1MOm+WJS\n" +
            "CWi1d/0CgYEA818bKc9aTsGOmc728MO0aEi7d1SbGJyG46KuEopIa+j4poFJTvLU\n" +
            "2wvaQEhNpgVeukIypxW3TuP8qeZszA/k3GisxDMVWrYpD1izWanM6EGhbdMPevUh\n" +
            "1PTlQce671XSbD49fTDcoLFu09XE1jqQU/EU6myvG320irWVE3fNIPMCgYEA3qBR\n" +
            "LnHu8184a4CJCYBUcFHDh4gxsDpJh9tXjvcCeeowdJk70kjIL7Di6NpX6ABLZjPO\n" +
            "sWQgRKG5cGTtxpKQLQA2QdnQ2saZAAS2yuPW8fzESyubiuJyugizolHcKO2PbI7f\n" +
            "7LHn9TGy3q+tksqvFhZ5tE2+rgVi92vgpynzxE8CgYBulZGPLvP3A0Zbp0pX3mVU\n" +
            "WXAtadlLlpxIRTxZmlIMDoElj2uTHw4PNlSGjxQRUzFW6wt/FoQDqd6+CMD4/GPe\n" +
            "rwWJ5ThXzpvbqE9ed6RvCJtkftny4f3seRbPDAVqCRIjMyjXgONPdTBJu0HEojnA\n" +
            "mrQJTTdIA5eMy7Ogc/hWdwKBgQCQrEQ1V4r+EyoaCkyDpSa6Wxgi0mnf6PDx3aOX\n" +
            "34N3cK4Oh6ntbKjS/TNoOMQZm19kSlSOyM+DakmU9bHjcklJRTL9NixYj+jLr0SO\n" +
            "suNzHFz/sJYC+keuB4uc92+IFWE4Hdz891wS5jokJqw4kYiYZQCwIDnC4vM+cJds\n" +
            "aoHkVwKBgQC/ObQzO2UxOzxI0/IbgKRm4a30uGUq/DAcrr7LbC4lQ0JpEnLhMK8r\n" +
            "26yCi9PhALxuYdTalkzT1nQbWxW8kDZuUYpYK3BDWJj2GxJfdCqNHkrnWZQb9A2x\n" +
            "Hq6/JLerN9xKQwFq+mqYDe7NPMCa2jin5Wc/EGQWzVzBt+dIEZE63w==\n" +
            "-----END RSA PRIVATE KEY-----";


    private static final String SERVER_URL = "http://api.dev.saferize";
    private static final String WEBSOCKET_URL = "ws://websocket.dev.saferize/usage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        signUpButton = (Button) findViewById(R.id.signUpButton);
        parentEmailText = (EditText) findViewById(R.id.parentEmailText);
        logTextView = (TextView) findViewById(R.id.logTextView);

        signUpButton.setOnClickListener(this::onSignUpButtonClicked);

        callback = new SaferizeCallback(new Handler(this.getMainLooper()));
        callback.onException(this::onException);
        callback.onSignedUp(this::onSignedUp);
        callback.onSessionCreated(this::onSessionCreated);
        callback.onConnect(this::onConnect);
        callback.onDisconnect(this::onDisconnect);
        callback.onError(this::onError);
        callback.onPause(this::onPause);
        callback.onResume(this::onResume);
        callback.onTimeUp(this::onTimeUp);
        userToken = "NewUserToken_" + new Date().getTime();
        bindService(new Intent(this, SaferizeService.class), this,  BIND_AUTO_CREATE);
    }

    private void onTimeUp(SaferizeSession saferizeSession) {
        logTextView.append("\n On TimeUp Received");
    }

    private void onResume(SaferizeSession saferizeSession) {
        logTextView.append("\n On Resume Received");
    }

    private void onPause(SaferizeSession saferizeSession) {
        logTextView.append("\n On Pause Received");
    }

    private void onError(SaferizeSession saferizeSession) {
        logTextView.append("\n On Error Received");
    }

    private void onDisconnect(SaferizeSession saferizeSession) {
        logTextView.append("\n On Disconnect Received");
    }

    private void onConnect(SaferizeSession saferizeSession) {
        logTextView.append("\n On Connect Received");
    }



    private void onSignUpButtonClicked(View view) {
        logTextView.append("\n Sign Up Button Clicked. Parent Email: " + parentEmailText.getText());
        try {
            Configuration config = new Configuration(new URI(SERVER_URL), new URI(WEBSOCKET_URL), "6b405c76-4b36-4821-9ab5-3a4a127b2af1", privateKey);
            saferizeService.init(config);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (WebsocketException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); }

        saferizeService.signUp(parentEmailText.getText().toString(), userToken, callback);
    }

    private void onSignedUp(Approval approval) {
        logTextView.append("\n Approval Received:" + approval.getStatus());
        saferizeService.createSession(userToken, callback);
    }

    private void onException(Exception e) {
        logTextView.append("\nEXCEPTION OCCURRED: " + e.getMessage());
    }

    private void onSessionCreated(SaferizeSession session) {
        logTextView.append("\nSession Created: " + session.getId());
        saferizeService.startWebSocket(callback);

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        saferizeService = ((SaferizeService.LocalBinder) service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        saferizeService = null;
    }
}
