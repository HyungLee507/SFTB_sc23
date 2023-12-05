package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.dto.SignUpDTO.FirstRegistUserInfoDto;
import CDProject.vfmarket.domain.dto.SignUpDTO.UserSignUpDto;
import CDProject.vfmarket.domain.dto.UserDTO.UserInfoDTO;
import CDProject.vfmarket.domain.entity.Role;
import CDProject.vfmarket.domain.entity.User;
import CDProject.vfmarket.exceptions.NotFoundException;
import CDProject.vfmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.rmi.NoSuchObjectException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception {
        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        } else {
            User user = User.builder()
                    .email(userSignUpDto.getEmail())
                    .password(userSignUpDto.getPassword())
                    .name(userSignUpDto.getName())
                    .footSize(userSignUpDto.getFootSize())
//                    .age(userSignUpDto.getAge())
                    .role(Role.MEMBER)
                    .build();
            user.passwordEncode(passwordEncoder);
            userRepository.save(user);
        }

    }

    public void signOut(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.deleteById(user.get().getId());
            log.info("회원 탈퇴 성공!! db 삭제");
        }
        if (user.isEmpty()) {
            log.info("회원 정보 가져오기 실패!");
        }

    }

    public void findPassword(String email, String newPassword) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new Exception("존재하지 않는 이메일입니다.");
        } else {
            User user = optionalUser.get();
            updatePassword(user, newPassword);
        }
    }

//    public int checkPassword(String email, String password) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            // 이메일이 없음
//            return -1;
//        } else {
//            // 이메일이 있음
//            User user = optionalUser.get();
//            User tempUser = User.builder().password(password).build();
//            tempUser.passwordEncode(passwordEncoder);
//            String tempPassword = tempUser.getPassword();
//            log.info("password: {}", user.getPassword());
//            log.info("tempPassword: {}", tempPassword);
//            if (user.getPassword().equals(tempPassword)){
//                // 임시 비밀번호 인증 성공
//                return 1;
//            } else {
//                // 임시 인증 실패
//                return 0;
//            }
//        }
//    }

    public void updatePassword(User user, String password) {
        user.updatePassword(password, passwordEncoder);
        userRepository.save(user);
    }

    public User findEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new Exception("존재하지 않는 이메일입니다.");
        } else {
            return optionalUser.get();
        }
    }

    public void updateUserInfo(FirstRegistUserInfoDto firstRegistUserInfoDto) throws NoSuchObjectException {
        Optional<User> firstLoginUser = userRepository.findByEmail(firstRegistUserInfoDto.getEmail());
        if (firstLoginUser.isPresent()) {
            firstLoginUser.get().authorizeUser();
            firstLoginUser.get().updateNickname(firstRegistUserInfoDto.getNickName());
            firstLoginUser.get().setFootSize(firstRegistUserInfoDto.getShoeSize());
        } else {
            throw new NoSuchObjectException("해당 유저를 찾을 수 없습니다.");
        }
    }

    public UserInfoDTO userInformation(Long ussrId) {
        User user = userRepository.findById(ussrId).orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(user.getName());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setSize(user.getFootSize());
        return userInfoDTO;
    }
}
