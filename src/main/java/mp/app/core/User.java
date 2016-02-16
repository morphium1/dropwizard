package mp.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class User {
    @NotNull
    @Id
    private String id;

    private String firstname;
    @NotNull
    private String lastname;

    @Valid
    private Email email;

    @JsonCreator
    public User(@Id @JsonProperty("id") String id, @JsonProperty("lastname")String lastname) {
        this.id = id;
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
