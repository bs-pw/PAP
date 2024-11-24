package pap.z27.papapi.domain.subclasses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Password {
    String password;
    public Password(String password) {this.password = password;}
}
