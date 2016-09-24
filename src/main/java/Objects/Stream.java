
package Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.Objects;

@Generated("org.jsonschema2pojo")
public class Stream {

    @SerializedName("_id")
    @Expose
    public Long id;
    @SerializedName("game")
    @Expose
    public String game;
    @SerializedName("viewers")
    @Expose
    public Integer viewers;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("video_height")
    @Expose
    public Integer videoHeight;
    @SerializedName("average_fps")
    @Expose
    public Float averageFps;
    @SerializedName("delay")
    @Expose
    public Integer delay;
    @SerializedName("is_playlist")
    @Expose
    public Boolean isPlaylist;
    @SerializedName("_links")
    @Expose
    public Stream_Links streamLinks;
    @SerializedName("channel")
    @Expose
    public Channel channel;

    public boolean isLive() {
        return channel != null;
    }

    public String getName() {
        return channel.getName();
    }

    public int getViews() {
        return viewers;
    }

    public String getUrl() {
        return channel.getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return getName().equals(stream.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
