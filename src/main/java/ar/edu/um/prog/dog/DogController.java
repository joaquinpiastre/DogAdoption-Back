package ar.edu.um.prog.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Autowired
    private DogService dogService;

    // Endpoint para agregar un perro
    @PostMapping("/add")
    public ResponseEntity<?> addDog(@RequestBody Dog dog) {
        try {
            Dog savedDog = dogService.saveDog(dog);
            return ResponseEntity.ok(savedDog);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding dog: " + e.getMessage());
        }
    }

    // Endpoint para obtener todos los perros
    @GetMapping("/all")
    public ResponseEntity<List<Dog>> getAllDogs() {
        List<Dog> dogs = dogService.getAllDogs();
        return ResponseEntity.ok(dogs);
    }
    
    // Endpoint para eliminar un perro

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        try {
            dogService.deleteDog(id);
            return ResponseEntity.ok("Dog deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting dog: " + e.getMessage());
        }
    }
}
