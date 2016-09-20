package Domain;

import Objects.Stream;
import Objects.StreamList;
import Objects.TopGames;
import com.google.gson.Gson;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Twitch {

    public static final String CLIENT_ID = "aaexx7qumq1mqaam3eqyu12ss0a38q0";
    private String authToken;
    private Gson gson;
    private Settings setting = Settings.getInstance();

    public static final String AUTH_URL = "https://api.twitch.tv/kraken/oauth2/authorize?response_type=token"
            + "&client_id=" + CLIENT_ID
            + "&redirect_uri=http://localhost/twitch_oauth"
            + "&scope=user_read";

    public Twitch(String authToken) {
        this.authToken = authToken;
        this.gson = new Gson();
    }

    public List<String> getGamesList() {
        String reponse = sendRequest("https://api.twitch.tv/kraken/games/top?limit=100");
        TopGames topGames = gson.fromJson(reponse, TopGames.class);
        return topGames.getGames();
    }

    public List<Stream> getRLStreams() throws SocketTimeoutException{
        String response = sendRequest("https://api.twitch.tv/kraken/streams?game=" + setting.getGame().replace(" ", "+") + "&limit=10");
        StreamList streams = gson.fromJson(response, StreamList.class);
        return streams.getStreams();
    }

    public List<Stream> getFollowedStreams() throws SocketTimeoutException{
        String response = sendRequest("https://api.twitch.tv/kraken/streams/followed?limit=100");
        StreamList streams = gson.fromJson(response, StreamList.class);
        return streams.getStreams();
    }

    private String sendRequest(String request) {
        try {
            return Request.Get(request)
                    .addHeader("Accept", "application/vnd.twitchtv.v3+json")
                    .addHeader("Authorization", "OAuth " + authToken)
                    .addHeader("Client-ID", CLIENT_ID)
                    .execute()
                    .returnContent().asString();
        } catch (HttpResponseException hre) {
            if (hre.getStatusCode() == 401 || hre.getStatusCode() == 403) {
                throw new UnauthorizedException(hre.getMessage());
            } else {
                throw new RuntimeException("could not request users streams", hre);
            }
        } catch (IOException e) {
            throw new RuntimeException("could not request users streams", e);
        }
    }


    public boolean isAuthValid() {
        try {
            getFollowedStreams();
            return true;
        } catch (UnauthorizedException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        }
    }

    public void openUrl(String url) {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    URI uri = new URI(url);
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
