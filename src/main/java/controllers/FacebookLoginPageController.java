package controllers;

import AllJumbledUp.GameManager;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * Created by enea.
 * Date: 11/5/15.
 * Time: 2:51 AM.
 */

public class FacebookLoginPageController {

    @FXML
    ScrollPane WebContent;

    // Reference to the main application.
    private GameManager gameManager;

    static boolean isTokenFound = false;

    public FacebookLoginPageController() {
    }

    public void bind () {

        gameManager.facebook = new FacebookFactory().getInstance();

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        WebContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        WebContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        WebContent.setFitToWidth(true);
        WebContent.setFitToHeight(true);

        WebContent.setContent(browser);
        webEngine.setJavaScriptEnabled(true);

        // Load Login Page
        webEngine.load(generateRequest());

        final StringBuffer code = new StringBuffer();

        // Captures response
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
                                String[] access_token = generateAccessToken(code.toString());

                                if (access_token[0].length() > 0) {
                                    isTokenFound = true;
                                    gameManager.setFBOAuthAccessToken(new AccessToken(access_token[0]));
                                    gameManager.startSession();
                                    gameManager.showMainMenuScene();
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

    private String generateRequest () {
        InputStream inputStream;
        String request = "";

        try {
            Properties prop = new Properties();
            String propFileName = "facebook4j.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null)
                prop.load(inputStream);
            else
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");

            // Request Params
            request = "https://www.facebook.com/dialog/oauth?" +
                    "client_id=" + prop.getProperty("oauth.appId") +
                    "&response_type=code" +
                    "&redirect_uri=https://www.facebook.com/connect/login_success.html" +
                    "&scope=publish_actions";

            inputStream.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return request;
    }

    private String[] generateAccessToken (String code) {
        InputStream inputStream;
        String[] access_token = new String[2];

        try {
            Properties prop = new Properties();
            String propFileName = "facebook4j.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null)
                prop.load(inputStream);
            else
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");

            access_token = sendGet("https://graph.facebook.com/v2.3/oauth/access_token?" +
                    "redirect_uri=https://www.facebook.com/connect/login_success.html" +
                    "&client_id=" + prop.getProperty("oauth.appId") +
                    "&client_secret=" + prop.getProperty("oauth.appSecret") +
                    "&code=" + code);

            inputStream.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return access_token;
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


    public void setMainApp(GameManager gameManagerApp) {
        System.out.println("GameSceneController started");
        this.gameManager = gameManagerApp;
        bind();
    }
}
