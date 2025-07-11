package project.TP5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.TP5.models.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    
}
