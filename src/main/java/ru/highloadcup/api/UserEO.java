package ru.highloadcup.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

public class UserEO {

    @Size(max = 100)
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    @Size(max = 50)
    @JsonProperty("first_name")
    private String firstName;

    @Size(max = 50)
    @JsonProperty("last_name")
    private String lastName;

    private User.Gender gender;

    @Min(value = -1262304000L)
    @Max(value = 915148800L)
    @JsonProperty("birth_date")
    private Long birthDate;

    @JsonCreator
    public UserEO(Map<String, Object> props) {
        for (Object value : props.values()) {
            if (value == null) throw new IllegalArgumentException();
        }
        this.email = (String) props.get("email");
        this.firstName = (String) props.get("first_name");
        this.lastName = (String) props.get("last_name");
        String gender = (String) props.get("gender");
        if (gender != null) {
            this.gender = User.Gender.valueOf(gender);
        }
        Object birth_date = props.get("birth_date");
        if (birth_date != null) {
            this.birthDate = Long.valueOf(String.valueOf(birth_date));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }
}
