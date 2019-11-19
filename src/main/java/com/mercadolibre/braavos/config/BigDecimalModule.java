package com.mercadolibre.braavos.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.mongojack.internal.object.document.DocumentObjectGenerator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;

public class BigDecimalModule extends SimpleModule {
    public BigDecimalModule() {
        super("BigDecimalModule");
        this.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        this.addSerializer(BigDecimal.class, new BigDecimalSerializer());
    }

    class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
        BigDecimalDeserializer() {
        }

        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new BigDecimal(p.getEmbeddedObject().toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
        BigDecimalSerializer() {
        }

        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.setScale(2, RoundingMode.HALF_UP));
        }
    }
}

