package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.UserSignUpDto;
import CDProject.vfmarket.domain.entity.Role;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception {
        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByName(userSignUpDto.getName()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .name(userSignUpDto.getName())
                .age(userSignUpDto.getAge())
                .role(Role.GUEST)
                .build();
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public void findPassword(String email, String newPassword) throws Exception{
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new Exception("존재하지 않는 이메일입니다.");
        } else{
            User user = optionalUser.get();
            updatePassword(user, newPassword);
        }
    }

    public void updatePassword(User user, String password) throws Exception{
        user.updatePassword(password, passwordEncoder);
        userRepository.save(user);
    }

    public User findEmail(String email) throws Exception{
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new Exception("존재하지 않는 이메일입니다.");
        } else{
            return optionalUser.get();
        }
    }
}
