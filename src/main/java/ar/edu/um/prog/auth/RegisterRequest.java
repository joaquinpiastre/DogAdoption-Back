package ar.edu.um.prog.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    String email;
    String password;
    String name; 
}
