package ma.hahn.productcrud.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.hahn.productcrud.dtos.AuthRequest;
import ma.hahn.productcrud.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private  final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request,
                                   HttpServletResponse response) {
        userService.login(request, response);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        var userInfo = userService.getCurrentUserInfo(request);
        return userInfo != null
                ? ResponseEntity.ok(userInfo)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("User Not Authenticated");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }
}