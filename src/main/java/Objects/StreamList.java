
package Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class StreamList {

    @SerializedName("streams")
    @Expose
    public List<Stream> streams = new ArrayList<Stream>();
    @SerializedName("_total")
    @Expose
    public Integer total;
    @SerializedName("_links")
    @Expose
    public TopStreams_Links links;

    public List<Stream> getStreams() {
        return streams;
    }
}
