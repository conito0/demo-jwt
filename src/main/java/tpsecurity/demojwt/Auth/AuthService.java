package tpsecurity.demojwt.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public AuthResponse login(LoginRequest request) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())); //springsecurity
            UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
            String token=jwtService.getToken(user);
            return AuthResponse.builder() //DTO
                    .token(token)
                    .build();

        }

        public AuthResponse register(RegisterRequest request) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode( request.getPassword()))
                    .firstname(request.getFirstname())
                    .role(Role.USER)
                    .country(request.getCountry())
                    .lastname(request.lastname)
                    .build();

            userRepository.save(user);

            return AuthResponse.builder()
                    .token(jwtService.getToken(user))
                    .build();

    }

}
