package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.StringProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class StringPropertyDeserializer extends StdDeserializer<StringProperty> {

    public StringPropertyDeserializer() {
        this(null);
    }

    public StringPropertyDeserializer(Class<StringProperty> vc) {
        super(vc);
    }

    @Override
    public StringProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        String value = node.get("value").textValue();

        return new StringProperty(nameId, value);
    }
}
