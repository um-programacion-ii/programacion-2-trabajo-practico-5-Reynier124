package project.TP5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.TP5.models.Proyecto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByFechaFinAfter(LocalDate fechaFin);
}
