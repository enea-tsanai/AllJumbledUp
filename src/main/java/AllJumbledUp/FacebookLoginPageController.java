package AllJumbledUp;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by enea on 11/5/15.
 */
public class FacebookLoginPageController {

    @FXML
    ScrollPane WebContent;

    // Reference to the main application.
    private AllJumbledUp allJumbledUp;

    static boolean isTokenFound = false;

    public FacebookLoginPageController() {
    }

    public void bind () {

        AllJumbledUp.facebook = new FacebookFactory().getInstance();

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        WebContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        WebContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        WebContent.setFitToWidth(true);
        WebContent.setFitToHeight(true);


        WebContent.setContent(browser);
        webEngine.setJavaScriptEnabled(true);

        // Request Params
        String client_id = "1639583132962407";
        String response_type = "code";
        String redirect_uri = "https://www.facebook.com/connect/login_success.html";
        String request = "https://www.facebook.com/dialog/oauth?" +
                            "client_id=" + client_id +
                            "&response_type=" + response_type +
                            "&redirect_uri=" + redirect_uri;

        // Load Login Page
        webEngine.load(request);

        final StringBuffer code = new StringBuffer();

        // Capture response
        webEngine.setOnStatusChanged(event -> {
            if ((event.getSource() instanceof WebEngine) && (!isTokenFound)) {

                WebEngine we = (WebEngine) event.getSource();
                String location = we.getLocation();

                System.out.println("Location: " + location);

                if (location.split("\\?").length > 1) {
                    String[] params = location.split("\\?")[1].split("&");

                    for (String s : params) {

                        // Get response code
                        if (s.split("=")[0].equals("code")) {
                            code.append(s.split("=")[1]);

                            // Get access token
                            try {
                                String[] access_token = sendGet("https://graph.facebook.com/v2.3/oauth/access_token?" +
                                        "redirect_uri=" + redirect_uri +
                                        "&client_id=" + client_id +
                                        "&client_secret=da0045843cca73f13f7832c088f39bb0" +
                                        "&code=" + code.toString());

                                if (access_token[0].length() > 0) {
                                    isTokenFound = true;
                                    AllJumbledUp.facebook.setOAuthAccessToken(new AccessToken(access_token[0]));
                                    AllJumbledUp.session = new Session(AllJumbledUp.facebook.getMe().getId(),
                                            AllJumbledUp.facebook.getMe().getName(),
                                            AllJumbledUp.facebook.getPictureURL(500, 500).toString());
                                    System.out.println(AllJumbledUp.session);
                                    allJumbledUp.showMainMenuScene();
                                }

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }


    // HTTP GET request
    private String[] sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response: " + response);
        String tmp = response.toString().replace("{", "");
        tmp = tmp.replace("}", "");
        tmp = tmp.replace("\"", "");
        String[] params = tmp.split(",");
        String[] AccessToken = new String[3];

        //print result
        for (String param: params) {
            if (param.split(":")[0].equals("access_token")) {
                System.out.println("Access Token: " + param.split(":")[1]);
                AccessToken[0] = param.split(":")[1];
            }
            if (param.split(":")[0].equals("token_type")) {
                System.out.println("Token Type: " + param.split(":")[1]);
                AccessToken[1] = param.split(":")[1];
            }
            if (param.split(":")[0].equals("expires_in")) {
                System.out.println("Expires In: " + param.split(":")[1]);
                AccessToken[2] = param.split(":")[1];
            }
        }
        return AccessToken;

//        if (response.toString().split(":")[0].equals("access_token"))
//            System.out.println("Response: " + response.toString().split(":")[1]);
//            return response.toString().split(":")[1];
//        else
//            return "no_result";
    }


    public void setMainApp(AllJumbledUp allJumbledUpApp) {
        System.out.println("Controller started");
        this.allJumbledUp = allJumbledUpApp;
        bind();
    }
}
