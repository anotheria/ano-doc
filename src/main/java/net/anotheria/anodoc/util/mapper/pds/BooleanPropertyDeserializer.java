package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.BooleanProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class BooleanPropertyDeserializer extends StdDeserializer<BooleanProperty> {

    public BooleanPropertyDeserializer(){
        this(null);
    }

    public BooleanPropertyDeserializer(Class<BooleanProperty> vc) {
        super(vc);
    }

    @Override
    public BooleanProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        boolean value = node.get("value").booleanValue();

        return new BooleanProperty(nameId, value);
    }
}
