package Domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Type;

/**
 * Created by Austin on 7/24/2016.
 */
public class BooleanPropertySerializer implements JsonSerializer<SimpleBooleanProperty> {
    @Override
    public JsonElement serialize(SimpleBooleanProperty src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new Gson();
        return gson.toJsonTree(src.get());
    }
}
