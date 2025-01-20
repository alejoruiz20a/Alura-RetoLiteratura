package com.alurachallenge.literalura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alurachallenge.literalura.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a JOIN FETCH a.libros")
    List<Autor> ListarTodosLosAutores();
    @Query("SELECT a FROM Autor a JOIN FETCH a.libros WHERE a.fechaNacimiento <= :year AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento > :year)")
    List<Autor> ListarAutoresPorAno(String year);

}
