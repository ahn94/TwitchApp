
package Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class TopGames {

    @SerializedName("top")
    @Expose
    private List<Top> top = new ArrayList<Top>();

    public List<Top> getTop() {
        return top;
    }

    public List<String> getGames() {
        List<String> games = new ArrayList<>();
        for (Top t : top) {
            games.add(t.getGame().getName());
        }
        return games;
    }

}
