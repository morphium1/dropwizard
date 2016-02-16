package mp.app.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import mp.app.core.Email;

import java.io.IOException;

public class EmailSerializer extends StdSerializer<Email> {

    protected EmailSerializer() {
        super(Email.class);
    }

    @Override
    public void serialize(Email value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getEmail());
    }
}
