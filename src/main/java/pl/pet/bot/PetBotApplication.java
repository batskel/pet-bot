package pl.pet.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PetBotApplication is the main class of the PetBot application.
 * It is responsible for starting the Spring Boot application.
 * To start the application, call the main method with appropriate arguments.
 */
@SpringBootApplication
public class PetBotApplication {

  /**
   * The main method is the entry point of the PetBot application.
   * It starts the Spring Boot application by invoking the SpringApplication.run() method.
   *
   * @param args the command-line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(PetBotApplication.class, args);
  }

}
