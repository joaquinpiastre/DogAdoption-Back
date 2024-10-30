package ar.edu.um.prog.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    public Dog saveDog(Dog dog) {
        return dogRepository.save(dog);
    }

    public List<Dog> getAllDogs() {
        List<Dog> dogs = new ArrayList<>();
        dogRepository.findAll().forEach(dogs::add); // Convertir Iterable a List
        return dogs;
    }

    public Dog getDogById(Long id) {
        return dogRepository.findById(id).orElse(null);
    }

    public void deleteDog(Long id) {
        dogRepository.deleteById(id);
    }

    // Otros métodos según sea necesario
}
