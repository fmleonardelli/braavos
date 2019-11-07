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
import java.time.Instant;
import java.util.Date;

public class CustomDateModule extends SimpleModule {
    public CustomDateModule() {
        super("CustomDateModule");
        this.addSerializer(Instant.class, new CustomDateModule.InstantJsonSerializer());
        this.addSerializer(Date.class, new CustomDateModule.DateJsonSerializer());
        this.addDeserializer(Instant.class, new CustomDateModule.InstantJsonDeserializer());
    }

    class DateJsonSerializer extends JsonSerializer<Date> {
        DateJsonSerializer() {
        }

        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (gen instanceof DocumentObjectGenerator) {
                gen.writeObject(value);
            } else {
                gen.writeString(value.toString());
            }

        }
    }

    class InstantJsonDeserializer extends JsonDeserializer<Instant> {
        InstantJsonDeserializer() {
        }

        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return ((Date)p.getEmbeddedObject()).toInstant();
        }
    }

    class InstantJsonSerializer extends JsonSerializer<Instant> {
        InstantJsonSerializer() {
        }

        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(Date.from(value));
        }
    }
}