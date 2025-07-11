package project.TP5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.TP5.models.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
