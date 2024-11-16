package jhappscode.tienda_libros;

import jhappscode.tienda_libros.vista.LibroForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class TiendaLibrosApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contextSpring =
				new SpringApplicationBuilder(TiendaLibrosApplication.class).headless(false).web(WebApplicationType.NONE).run(args);


		// Ejecutar el codigo para cargar el formulario
		EventQueue.invokeLater(()->{
			// Obtenemos el objeto forma atraves de Spring
			LibroForm libroForm = contextSpring.getBean(LibroForm.class);
			libroForm.setVisible(true);
		});
	}

}
