package com.alurachallenge.literalura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alurachallenge.literalura.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainingIgnoreCase (String nombreTitulo);
    @Query("SELECT l FROM Libro l WHERE :idioma = l.lenguajes")
    List<Libro> ListarLibrosPorIdioma(@Param("idioma") List<String> idioma);


}
