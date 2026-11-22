package cl.huerto.inventario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

interface Registros extends JpaRepository<Insumo,Long> {}
record Entrada(@NotBlank @Size(max=100) String nombre, @NotNull @PositiveOrZero Integer cantidad) {}

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {
    private final Registros registros;
    public InsumoController(Registros registros) { this.registros=registros; }
    @GetMapping public List<Insumo> listar() { return registros.findAll(); }
    @GetMapping("/{id}") public Insumo buscar(@PathVariable long id) {
        return registros.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Insumo crear(@Valid @RequestBody Entrada entrada) { return guardar(new Insumo(),entrada); }
    @PutMapping("/{id}") public Insumo actualizar(@PathVariable long id,@Valid @RequestBody Entrada entrada) {
        return guardar(buscar(id),entrada);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) { registros.delete(buscar(id)); }
    private Insumo guardar(Insumo fila, Entrada entrada) {
        fila.nombre=entrada.nombre().trim();
        fila.cantidad=entrada.cantidad();
        return registros.save(fila);
    }
}
