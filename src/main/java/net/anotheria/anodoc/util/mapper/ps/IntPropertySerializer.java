package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.IntProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class IntPropertySerializer extends StdSerializer<IntProperty> {

    public IntPropertySerializer() {
        this(null);
    }

    public IntPropertySerializer(Class<IntProperty> t) {
        super(t);
    }

    @Override
    public void serialize(IntProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeNumberField("value", value.getInt());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
