package nl.noviaal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class NoviaalApplicationTests {

  @Autowired
  private Environment env;

	@Test
	void contextLoads() {
	  assertThat(env.getActiveProfiles()).contains("test");
	}

}
