package mp.app.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import mp.app.core.Email;

import java.io.IOException;

public class EmailDeserializer extends StdDeserializer<Email> {

    protected EmailDeserializer() {
        super(Email.class);
    }

    @Override
    public Email deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Email(p.readValueAs(String.class));
    }
}
