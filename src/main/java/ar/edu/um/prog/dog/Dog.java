package ar.edu.um.prog.dog;

import ar.edu.um.prog.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.Entity;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.Table;

import jakarta.persistence.GeneratedValue;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="dogs")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;
    private Integer age;  // Edad del perro
    private Double weight;  // Peso del perro
    private String gender;  // Género del perro
    private String contactNumber;  // Número de contacto del dueño
    private String description;  // Descripción del perro

    // Relación con el propietario del perro
    @ManyToOne
    @JoinColumn(name = "user_id") // Nombre de la columna en la base de datos para la clave foránea
    private User user;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }
    
    public Integer getAge() {
        return age;
    }

    public Double getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDescription() {
        return description;
    }
    
}
