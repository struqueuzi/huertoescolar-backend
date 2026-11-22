package cl.huerto.agenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

interface Registros extends JpaRepository<Jornada,Long> {}
record Entrada(@NotBlank @Size(max=100) String nombre, @NotNull java.time.LocalDate fecha) {}

@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {
    private final Registros registros;
    public JornadaController(Registros registros) { this.registros=registros; }
    @GetMapping public List<Jornada> listar() { return registros.findAll(); }
    @GetMapping("/{id}") public Jornada buscar(@PathVariable long id) {
        return registros.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Jornada crear(@Valid @RequestBody Entrada entrada) { return guardar(new Jornada(),entrada); }
    @PutMapping("/{id}") public Jornada actualizar(@PathVariable long id,@Valid @RequestBody Entrada entrada) {
        return guardar(buscar(id),entrada);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) { registros.delete(buscar(id)); }
    private Jornada guardar(Jornada fila, Entrada entrada) {
        fila.nombre=entrada.nombre().trim();
        fila.fecha=entrada.fecha();
        return registros.save(fila);
    }
}
