package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class User implements Serializable, UserDetails, CredentialsContainer {
    private Integer user_id;
    private String name;
    private String surname;
    private Integer user_type_id;
    private String password;
    private String mail;
    public User(String name, String surname, String password, String mail, Integer user_type_id) {
        this.user_id = null;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.user_type_id = user_type_id;
//        this.password = password;
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return user_type_id.equals(4);
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !user_type_id.equals(4);
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}