package jhappscode.tienda_libros.repositorio;


import jhappscode.tienda_libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Integer> {



}
