package Domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Austin on 7/23/2016.
 */
public class Settings {

    public static final String CLIENT_ID = "aaexx7qumq1mqaam3eqyu12ss0a38q0";
    public static final String AUTH_URL = "https://api.twitch.tv/kraken/oauth2/authorize?response_type=token"
            + "&client_id=" + CLIENT_ID
            + "&redirect_uri=http://localhost/twitch_oauth"
            + "&scope=user_read";

    private static Logger log = (Logger) LogManager.getLogger(Settings.class.getName());

    private static final Path SETTINGS_DIR;
    private static final Path SETTINGS_FILE;
    private static Settings instance;
    private final static Gson mapper;

    private List<String> games = new ArrayList<>();

    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("updateInterval")
    @Expose
    private SimpleIntegerProperty updateInterval = new SimpleIntegerProperty(3);
    @SerializedName("game")
    @Expose
    private String game = "Rocket League";
    @SerializedName("notifications")
    @Expose
    private boolean notifications = true;


    static {
        mapper = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(SimpleIntegerProperty.class, new IntegerPropertyDeserializer())
                .registerTypeAdapter(SimpleBooleanProperty.class, new BooleanPropertyDeserializer())
                .registerTypeAdapter(SimpleBooleanProperty.class, new BooleanPropertySerializer())
                .registerTypeAdapter(SimpleIntegerProperty.class, new IntegerPropertySerializer())
                .serializeNulls()
                .setPrettyPrinting()
                .create()
        ;

        String userDir = System.getenv("LOCALAPPDATA");
        if (userDir != null) {
            SETTINGS_DIR = Paths.get(userDir, "Twitch");
        } else if ((userDir = System.getenv("XDG_CONFIG_HOME")) != null) {
            SETTINGS_DIR = Paths.get(userDir, "Twitch");
        } else {
            SETTINGS_DIR = Paths.get("./");
        }
        SETTINGS_FILE = SETTINGS_DIR.resolve("settings.conf");
        log.debug(SETTINGS_FILE.toString());
        try {
            Files.createDirectories(SETTINGS_DIR);
            try {
                String json = new String(Files.readAllBytes(SETTINGS_FILE));
                instance = mapper.fromJson(json, Settings.class);
            } catch (IOException e) {
                System.out.println("caught");
                instance = new Settings();
            }
        } catch (IOException e) {
            log.error("error creating settings directory", e);
            System.exit(-1);
        }

    }


    public SimpleIntegerProperty updateIntervalProperty() {
        return updateInterval;
    }

    private Settings() {
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        Collections.sort(games);
        this.games = games;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Integer getUpdateInterval() {
        return updateInterval.get();
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval.set(updateInterval);
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public static Settings getInstance() {
        return instance;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SETTINGS_FILE.toFile()))) {
            bw.write(mapper.toJson(this));
        }catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
