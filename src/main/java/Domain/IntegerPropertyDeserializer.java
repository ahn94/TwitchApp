package Domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.Type;

public class IntegerPropertyDeserializer implements JsonDeserializer<SimpleIntegerProperty> {
    @Override
    public SimpleIntegerProperty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SimpleIntegerProperty integer = new SimpleIntegerProperty();
        integer.set(json.getAsInt());
        return integer;
    }
}
