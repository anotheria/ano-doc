package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.DoubleProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class DoublePropertySerializer extends StdSerializer<DoubleProperty> {

    public DoublePropertySerializer() {
        this(null);
    }

    public DoublePropertySerializer(Class<DoubleProperty> t) {
        super(t);
    }

    @Override
    public void serialize(DoubleProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeNumberField("value", value.getdouble());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
