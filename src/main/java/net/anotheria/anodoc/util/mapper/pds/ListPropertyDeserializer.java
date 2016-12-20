package net.anotheria.anodoc.util.mapper.pds;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.anotheria.anodoc.data.ListProperty;
import net.anotheria.anodoc.data.Property;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author ykalapusha
 */
public class ListPropertyDeserializer extends StdDeserializer<ListProperty> {

    public ListPropertyDeserializer() {
        this(null);
    }

    public ListPropertyDeserializer(Class<ListProperty> vc) {
        super(vc);
    }

    @Override
    public ListProperty deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode node = mapper.readTree(jp);
        String nameId = node.fieldNames().next();

        Property[] properties = mapper.readValue(node.elements().next().toString().getBytes("UTF-8"), Property[].class);
        return new ListProperty(nameId, Arrays.asList(properties));
    }
}
