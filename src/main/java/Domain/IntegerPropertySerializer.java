package Domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.Type;

/**
 * Created by Austin on 7/24/2016.
 */
public class IntegerPropertySerializer implements JsonSerializer<SimpleIntegerProperty> {
    @Override
    public JsonElement serialize(SimpleIntegerProperty src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new Gson();
        return gson.toJsonTree(src.get());
    }
}
