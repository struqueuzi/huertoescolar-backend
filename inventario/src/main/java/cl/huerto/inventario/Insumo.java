package cl.huerto.inventario;
import jakarta.persistence.*;
@Entity
public class Insumo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    public String nombre;
    public Integer cantidad;
}
