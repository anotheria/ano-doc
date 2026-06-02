package net.anotheria.anodoc.util.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.anotheria.anodoc.data.*;
import net.anotheria.anodoc.util.mapper.pds.*;
import net.anotheria.anodoc.util.mapper.ps.*;


/**
 * Utility for serialization and deserialization of asg documents.
 *
 * @author ykalapusha
 */
public final class ObjectMapperUtil {

    /**
     * {@link ObjectMapper} instance.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Configuration for {@link ObjectMapper} instance
     */
    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.registerModule(createCustomModule());
    }

    /**
     * Construct {@link SimpleModule} of serialization deserialization process for {@link ObjectMapper} instance.
     * @return  {@link SimpleModule} configured instance
     */
    private static Module createCustomModule() {
        final SimpleModule module = new SimpleModule();
        //add serializers
        module.addSerializer(BooleanProperty.class, new BooleanPropertySerializer());
        module.addSerializer(DoubleProperty.class, new DoublePropertySerializer());
        module.addSerializer(FloatProperty.class, new FloatPropertySerializer());
        module.addSerializer(IntProperty.class, new IntPropertySerializer());
        module.addSerializer(ListProperty.class, new ListPropertySerializer());
        module.addSerializer(LongProperty.class, new LongPropertySerializer());
        module.addSerializer(StringProperty.class, new StringPropertySerializer());
        //add deserializers
        module.addDeserializer(BooleanProperty.class, new BooleanPropertyDeserializer());
        module.addDeserializer(DoubleProperty.class, new DoublePropertyDeserializer());
        module.addDeserializer(FloatProperty.class, new FloatPropertyDeserializer());
        module.addDeserializer(IntProperty.class, new IntPropertyDeserializer());
        module.addDeserializer(ListProperty.class, new ListPropertyDeserializer());
        module.addDeserializer(LongProperty.class, new LongPropertyDeserializer());
        module.addDeserializer(Property.class, new PropertyDeserializer());
        module.addDeserializer(StringProperty.class, new StringPropertyDeserializer());

        return module;
    }

    /**
     * Default constructor.
     */
    private ObjectMapperUtil () { }

    /**
     * Getter for {@link ObjectMapper} instance.
     * @return  {@link ObjectMapper} instance
     */
    public static ObjectMapper getMapperInstance() {
        return MAPPER;
    }
}
