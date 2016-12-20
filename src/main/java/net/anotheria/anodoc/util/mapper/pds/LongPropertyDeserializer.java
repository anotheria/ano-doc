package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.LongProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class LongPropertyDeserializer extends StdDeserializer<LongProperty> {

    public LongPropertyDeserializer() {
        this(null);
    }

    public LongPropertyDeserializer(Class<LongProperty> vc) {
        super(vc);
    }

    @Override
    public LongProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        long value = node.get("value").longValue();

        return new LongProperty(nameId, value);
    }
}
