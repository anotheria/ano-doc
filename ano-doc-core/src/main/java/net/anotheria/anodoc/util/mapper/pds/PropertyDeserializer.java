package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.anotheria.anodoc.data.*;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class PropertyDeserializer extends StdDeserializer<Property> {

    public PropertyDeserializer(){
        this(null);
    }

    public PropertyDeserializer(Class<Property> vc) {
        super(vc);
    }

    @Override
    public Property deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        String type = root.get("type").asText();

        return mapper.readValue(root.toString().getBytes("UTF-8"), PropertyType.byName(type).getClazz());
    }
}
