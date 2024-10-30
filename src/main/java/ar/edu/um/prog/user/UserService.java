package ar.edu.um.prog.user;

import ar.edu.um.prog.dog.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Asegúrate de inyectar el encoder para encriptar contraseñas

    public ArrayList<User> getUser() {
        return (ArrayList<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptación de la contraseña al guardar
        return userRepository.save(user);
    }

    public Boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<User> updateUser(User request, Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userToSave = user.get();
            userToSave.setName(request.getName());
            userToSave.setEmail(request.getEmail());

            if (!request.getPassword().isEmpty()) {  // Encriptación solo si se cambia la contraseña
                userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            return Optional.of(userRepository.save(userToSave));
        }
        return Optional.empty();
    }

    public Optional<User> getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email);
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
