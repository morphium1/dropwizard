package mp.app.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mp.app.jackson.EmailDeserializer;
import mp.app.jackson.EmailSerializer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static mp.app.validation.ValidationPatterns.EMAIL_PATTERN;

@JsonSerialize(using = EmailSerializer.class)
@JsonDeserialize(using = EmailDeserializer.class)
public class Email {

    @NotNull
    @Size(max = 254)
    @Pattern(regexp = EMAIL_PATTERN)
    private String email;

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
