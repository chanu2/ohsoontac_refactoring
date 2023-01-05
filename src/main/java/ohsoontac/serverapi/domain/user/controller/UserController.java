package ohsoontac.serverapi.domain.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ohsoontac.serverapi.domain.user.dto.request.SignInDto;
import ohsoontac.serverapi.domain.user.dto.request.SignUpDto;
import ohsoontac.serverapi.domain.user.entity.User;
import ohsoontac.serverapi.domain.user.service.UserService;
import ohsoontac.serverapi.global.response.DefaultRes;
import ohsoontac.serverapi.global.response.StatusCode;
import ohsoontac.serverapi.global.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;




    // 아이디 중복체크
    @GetMapping("/checkUnique")
    public ResponseEntity check(@RequestParam(name = "userUid")String uid) {

        Boolean result = userService.checkUnique(uid);

        return result ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "사용가능한 아이디입니다"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "중복된 아이디입니다"), HttpStatus.OK);
    }

    // 회원가입 요청
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody SignUpDto user) {
        Long result = userService.signUp(user);

        return result != null ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "회원가입 요청을 성공하였습니다"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody SignInDto user, HttpServletResponse response) {

        Optional<User> member = userService.findUserByUid(user.getUid());

        if (member.isEmpty()) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "로그인 아이디 또는 비밀번호 오류입니다"), HttpStatus.OK);

        boolean checkPassword = userService.checkPassword(member.get(), user);

        if (!checkPassword) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "로그인 아이디 또는 비밀번호 오류입니다"), HttpStatus.OK);



        // 어세스, 리프레시 토큰 발급 및 헤더 설정
        String accessToken = jwtTokenProvider.createAccessToken(member.get().getUid(), member.get().getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.get().getUid(), member.get().getRoles());

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        // 리프레시 토큰 저장소에 저장
        userService.signIn(refreshToken, member.get());

        if (user.getUid().equals("admin")) return new ResponseEntity(DefaultRes.res(StatusCode.OK, "관리자 로그인 완료", "ROLE_ADMIN"), HttpStatus.OK);
        else if (user.getUid().equals("guest")) return new ResponseEntity(DefaultRes.res(StatusCode.OK, "게스트 로그인 완료", "ROLE_GUEST"), HttpStatus.OK);
        else return new ResponseEntity(DefaultRes.res(StatusCode.OK, "사용자 로그인 완료", "ROLE_USER"), HttpStatus.OK);
    }


    //로그아웃
    @PostMapping("/signOut")
    public ResponseEntity signOut(@RequestHeader("RefreshToken") String refreshToken, @RequestParam(name = "userUid")String userUid) {
        refreshToken = refreshToken.substring(7);
        User user = userService.findUserByUid(userUid).get();
        Boolean existAndOut = userService.signOut(refreshToken,user);

        return existAndOut ?
                new ResponseEntity(DefaultRes.res(StatusCode.OK, "로그아웃 완료"), HttpStatus.OK):
                new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, "잘못된 요청"), HttpStatus.OK);
    }

    // 내정보 보여주기

}