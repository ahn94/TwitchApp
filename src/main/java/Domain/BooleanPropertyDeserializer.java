package Domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Type;

/**
 * Created by Austin on 7/24/2016.
 */
public class BooleanPropertyDeserializer implements JsonDeserializer<SimpleBooleanProperty> {
    @Override
    public SimpleBooleanProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SimpleBooleanProperty simpleBoolean = new SimpleBooleanProperty();
        simpleBoolean.set(json.getAsBoolean());
        return simpleBoolean;
    }
}
