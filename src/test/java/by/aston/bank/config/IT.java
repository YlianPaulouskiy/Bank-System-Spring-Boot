package by.aston.bank.config;

import by.aston.bank.BankSystemApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@ExtendWith(PostgreSqlTestContainerExtension.class)
@SpringBootTest(classes = BankSystemApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IT {
}
