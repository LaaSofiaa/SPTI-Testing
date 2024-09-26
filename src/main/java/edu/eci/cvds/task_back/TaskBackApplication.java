package edu.eci.cvds.task_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que configura y ejecuta la aplicación Spring Boot.
 * La anotación {@code @SpringBootApplication} habilita la configuración automática,
 * el escaneo de componentes y permite el uso de configuraciones adicionales de Spring.
 */
@SpringBootApplication
public class TaskBackApplication {

	/**
	 * Método principal que se utiliza para lanzar la aplicación Spring Boot.
	 * @param args Argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskBackApplication.class, args);
	}

}
