package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.FloatProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class FloatPropertyDeserializer extends StdDeserializer<FloatProperty> {

    public FloatPropertyDeserializer() {
        this(null);
    }

    public FloatPropertyDeserializer(Class<FloatProperty> vc) {
        super(vc);
    }

    @Override
    public FloatProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        float value = node.get("value").floatValue();

        return new FloatProperty(nameId, value);
    }
}
