
package Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Top {

    @SerializedName("game")
    @Expose
    private Game game;

    public Game getGame() {
        return game;
    }


    public void setGame(Game game) {
        this.game = game;
    }



}
