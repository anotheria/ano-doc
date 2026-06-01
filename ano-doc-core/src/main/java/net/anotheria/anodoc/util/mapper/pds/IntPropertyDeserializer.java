package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.IntProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class IntPropertyDeserializer extends StdDeserializer<IntProperty> {

    public IntPropertyDeserializer() {
        this(null);
    }

    public IntPropertyDeserializer(Class<IntProperty> vc) {
        super(vc);
    }

    @Override
    public IntProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        int value = node.get("value").intValue();

        return new IntProperty(nameId, value);
    }
}
