package logen.common;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalTimeDeserialiser extends StdDeserializer<LocalTime> {
    protected LocalTimeDeserialiser() {
        super(LocalDate.class);
    }

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return LocalTime.parse(parser.readValueAs(String.class));
    }
}

