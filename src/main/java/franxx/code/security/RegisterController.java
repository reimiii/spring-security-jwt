package franxx.code.security;

import franxx.code.security.model.MyUser;
import franxx.code.security.model.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  @Autowired
  private MyUserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping(
      path = "/register/user",
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public MyUser createUser(@RequestBody MyUser user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return repository.save(user);
  }
}
