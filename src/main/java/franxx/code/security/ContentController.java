package franxx.code.security;

import franxx.code.security.model.MyUserDetailService;
import franxx.code.security.web.token.JwtService;
import franxx.code.security.web.token.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private MyUserDetailService userDetailService;

  @GetMapping("/home")
  public String handleWelcome() {
    return "Welcome to home!";
  }

  @GetMapping("/admin/home")
  public String handleAdminHome() {
    return "Welcome to ADMIN home!";
  }

  @GetMapping("/user/home")
  public String handleUserHome() {
    return "Welcome to USER home!";
  }

  @PostMapping(path = "/auth")
  public String login(
      @RequestBody LoginRequest request
  ) {

    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.username(), request.password()
    ));

    if (authenticate.isAuthenticated()) {
      return jwtService.generateToken(
          userDetailService.loadUserByUsername(request.username())
      );
    } else {
      throw new UsernameNotFoundException("invalid credential");
    }
  }

}