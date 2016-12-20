package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.LongProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class LongPropertySerializer extends StdSerializer<LongProperty> {

    public LongPropertySerializer() {
        this(null);
    }

    public LongPropertySerializer(Class<LongProperty> t) {
        super(t);
    }

    @Override
    public void serialize(LongProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeNumberField("value", value.getlong());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }
}
