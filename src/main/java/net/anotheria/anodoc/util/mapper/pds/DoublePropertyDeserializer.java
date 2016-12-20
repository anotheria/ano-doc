package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.anotheria.anodoc.data.DoubleProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class DoublePropertyDeserializer extends StdDeserializer<DoubleProperty> {

    public DoublePropertyDeserializer() {
        this(null);
    }

    public DoublePropertyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DoubleProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nameId = node.get("id").textValue();
        double value = node.get("value").doubleValue();

        return new DoubleProperty(nameId, value);
    }
}
