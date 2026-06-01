package net.anotheria.anodoc.util.mapper.ps;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.anotheria.anodoc.data.StringProperty;

import java.io.IOException;

/**
 * @author ykalapusha
 */
public class StringPropertySerializer extends StdSerializer<StringProperty> {

    public StringPropertySerializer() {
        this(null);
    }

    public StringPropertySerializer(Class<StringProperty> t) {
        super(t);
    }

    @Override
    public void serialize(StringProperty value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId());
        jgen.writeStringField("value", value.getString());
        jgen.writeStringField("type", value.getPropertyType().toString());
        jgen.writeEndObject();
    }

}
