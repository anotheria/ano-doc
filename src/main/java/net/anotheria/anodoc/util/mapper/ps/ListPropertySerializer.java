package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.ListProperty;
import net.anotheria.anodoc.data.Property;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class ListPropertySerializer extends StdSerializer<ListProperty> {

    public ListPropertySerializer() {
        this(null);
    }

    public ListPropertySerializer(Class<ListProperty> t) {
        super(t);
    }

    @Override
    public void serialize (ListProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeArrayFieldStart(value.getId());
        for (Property element: value.getList()) {
            jgen.writeObject(element);
        }
        jgen.writeEndArray();
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
