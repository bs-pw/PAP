package pap.z27.papapi.domain.subclasses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Credentials {
    private String mail;
    private String password;
}
