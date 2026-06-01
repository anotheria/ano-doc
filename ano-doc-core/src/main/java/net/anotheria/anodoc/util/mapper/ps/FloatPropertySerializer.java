package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.FloatProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class FloatPropertySerializer extends StdSerializer<FloatProperty>{

    public FloatPropertySerializer() {
        this(null);
    }

    public FloatPropertySerializer(Class<FloatProperty> t) {
        super(t);
    }

    @Override
    public void serialize(FloatProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeNumberField("value", value.getfloat());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
