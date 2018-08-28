package saferize.com.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.saferize.sdk.Approval;
import com.saferize.sdk.AuthenticationException;
import com.saferize.sdk.Configuration;
import com.saferize.sdk.SaferizeClient;
import com.saferize.sdk.SaferizeClientException;
import com.saferize.sdk.SaferizeSession;

import org.junit.Test;
import org.junit.runner.RunWith;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("saferize.com.sdk.test", appContext.getPackageName());
    }

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

    private static final String SERVER_URL = "http://10.0.2.2:8080";
    //private static final String SERVER_URL = "http://192.168.11.250";
    private static final String WEBSOCKET_URL = "ws://websocket.dev.saferize/usage";

    @Test
    public void testSignUp() throws SaferizeClientException, AuthenticationException, URISyntaxException {
        Configuration configuration = new Configuration(new URI("http://192.168.11.250" ), new URI("ws://websocket.dev.saferize/usage"), "6b405c76-4b36-4821-9ab5-3a4a127b2af1", privateKey);
        SaferizeClient client = new SaferizeClient(configuration);
        String userToken = "userToken" + new Date().getTime();
        Approval approval = client.signUp("renato+mine" + new Date().getTime() + "@saferize.com", userToken);
        assertThat(approval.getStatus(), is (Approval.Status.PENDING));

        SaferizeSession session = client.createSession(userToken);
        assertThat(session.getStatus(), is (SaferizeSession.Status.ACTIVE));
    }


    @Test
    public void testWebsocketClient() throws Exception {
        Configuration config = new  Configuration(new URI(SERVER_URL), new URI(WEBSOCKET_URL), "6b405c76-4b36-4821-9ab5-3a4a127b2af1", privateKey);
        SaferizeClient client = new SaferizeClient(config);
        String userToken = "userToken" + new Date().getTime();
        Approval approval = client.signUp("renato+mine" + new Date().getTime() + "@saferize.com", userToken);
        assertThat(approval.getStatus(), is (Approval.Status.PENDING));

        SaferizeSession session = client.createSession(userToken);
        assertThat(session.getStatus(), is (SaferizeSession.Status.ACTIVE));

        client.startWebsocketConnection();
        client.onConnect((data) -> {
            System.out.println(data.getId());
        });

        Thread.sleep(10000);

    }


}
