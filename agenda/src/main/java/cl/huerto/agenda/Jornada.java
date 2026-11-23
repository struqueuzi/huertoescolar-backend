package cl.huerto.agenda;
import jakarta.persistence.*;
@Entity
public class Jornada {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String nombre;
    public java.time.LocalDate fecha;
}
