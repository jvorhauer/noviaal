package nl.noviaal.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Command 'CreateUser' as passed to the AuthController
 * see https://blog.jdriven.com/2019/04/immutable-classes-with-spring-boot-lombok-and-jackson/
 * @author Jurjen Vorhauer
 * @since 2021-01-18
 */
@Value
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateUser {
  @NotBlank
  @Size(min = 3, max = 255)
  String name;

  @NotBlank
  @Email
  String email;

  @NotBlank
  @Size(min = 7, max = 72)
  String password;
}
