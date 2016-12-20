package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.BooleanProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class BooleanPropertySerializer extends StdSerializer<BooleanProperty> {

    public BooleanPropertySerializer() {
        this(null);
    }

    public BooleanPropertySerializer(Class<BooleanProperty> t) {
        super(t);
    }

    @Override
    public void serialize(BooleanProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeBooleanField("value", value.getboolean());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
