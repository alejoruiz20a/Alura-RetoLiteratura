package com.alurachallenge.literalura.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;

import com.alurachallenge.literalura.model.Autor;
import com.alurachallenge.literalura.model.DatosAutor;
import com.alurachallenge.literalura.model.DatosGenerales;
import com.alurachallenge.literalura.model.DatosLibro;
import com.alurachallenge.literalura.model.Libro;
import com.alurachallenge.literalura.repository.AutorRepository;
import com.alurachallenge.literalura.repository.LibroRepository;
import com.alurachallenge.literalura.service.ConsumoApi;
import com.alurachallenge.literalura.service.ConvierteDatos;

public class Principal {
    private List<Libro> libros;
    private List<Autor> autores;
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepository;
    private String tituloBuscado="";

    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu(){
        var opcion =-1;
        while (opcion != 0) {
            var menu = """
                    1. Buscar Libro Por Titulo
                    2. Listar Libros Registrados
                    3. Listar Autores Registrados
                    4. Listar Autores Vivos En Un Determinado Año
                    5. Listar Libros Por Idioma
                    
                    0. Salir
                    """;
            System.out.println(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarTodosLosLibros();
                    break;
                case 3:
                    listarTodosLosAutores();
                    break;
                case 4:
                    ListarAutoresPorAno();
                    break;
                case 5:
                    ListarLibrosPorLenguaje();
                    break;
                case 0:
                    System.out.println("Fin del programa - DEV: Juan José Jaramillo Vera");
                    break;
                default:
                    System.out.println("Opcion Invalida");
            }
        }
    }

    private DatosGenerales getDatosLibro() {
        // Buscar por nombre
        System.out.println("\nIngrese el nombre del libro que quiere buscar");
        tituloBuscado = sc.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloBuscado.replace(" ", "+"));
        DatosGenerales datosBusqueda = conversor.obtenerDatos(json, DatosGenerales.class);
        return datosBusqueda;
    }

    private void buscarLibro() {

        DatosGenerales datosGenerales = getDatosLibro();
        List<Autor> autores = new ArrayList<>();
        Optional<DatosLibro> libroBuscado =datosGenerales.libro().stream()
                .filter(l-> l.titulo().toUpperCase().contains(tituloBuscado.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){

            System.out.println("Libro Encontrado");

            for (DatosAutor datosAutor : libroBuscado.get().autor()) {
                Autor autor = new Autor();
                autor.setNombre(datosAutor.nombre());
                autor.setFechaNacimiento(datosAutor.fechaDeNacimiento());
                autor.setFechaFallecimiento(datosAutor.fechaDeFallecimiento());

                autores.add(autor);
            }
            Libro libro = new Libro(libroBuscado.get());
            libro.setAutores(autores);
            Optional<Libro> libroExistente = libroRepositorio.findByTituloContainingIgnoreCase(libroBuscado.get().titulo());
            if (!libroExistente.isPresent()) {

                try {
                    for (Autor autor : autores) {
                        autorRepository.save(autor);
                    }
                    libroRepositorio.save(libro);
                } catch (DataIntegrityViolationException e) {
                    System.out.println("Error: Libro ya existe en la base de datos");
                }
            }else {
                System.out.println("Libro Ya Existe En La Base De Datos\n");
            }
            System.out.println(libro.toString());
        }else {
            System.out.println("Libro No Encontrado");
        }

    }

    public void listarTodosLosLibros(){
        libros = libroRepositorio.findAll();
        libros.stream()
                .forEach(System.out::println);
    }

    public void listarTodosLosAutores(){
        autores= autorRepository.ListarTodosLosAutores();
        autores.stream()
                .forEach(System.out::println);
    }

    public List<Autor> ListarAutoresPorAno(){
        System.out.println("Ingrese el año de nacimiento de los autores que desea buscar:");
        String year = sc.nextLine();

        List<Autor> autores = autorRepository.ListarAutoresPorAno(year);

        List<Autor> autoresOrdenados = autores.stream()
                .sorted((a1, a2) -> {
                    int year1 = Integer.parseInt(a1.getFechaNacimiento().substring(1, 4));
                    int year2 = Integer.parseInt(a2.getFechaNacimiento().substring(1, 4));
                    return Integer.compare(year1, year2);
                })
                .collect(Collectors.toList());
        for (Autor autor : autoresOrdenados){
            System.out.println(autor.toString());
        }

        return autoresOrdenados;
    }

    public List<Libro> ListarLibrosPorLenguaje() {
        System.out.println("Ingrese el código del idioma en el que quiere buscar los libros:");
        System.out.println("es: Español");
        System.out.println("en: Inglés");
        System.out.println("fr: Francés");
        System.out.println("pt: Portugués");
        List<String> subMenuIdioma = new ArrayList<>(); // Convertir a minúsculas para manejar entradas en mayúsculas o minúsculas
        String idioma = sc.nextLine().toLowerCase();
        subMenuIdioma.add(0,idioma);
        List<Libro> librosEnIdiomaEspecifico = libroRepositorio.ListarLibrosPorIdioma(subMenuIdioma);

        if (librosEnIdiomaEspecifico.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma "+ obtenerNombreIdioma(subMenuIdioma));
        } else {
            System.out.println("Libros en " + obtenerNombreIdioma(subMenuIdioma) + ":");
            librosEnIdiomaEspecifico.forEach(libro -> System.out.println(libro.toString()));
        }

        return librosEnIdiomaEspecifico;
    }

    private String obtenerNombreIdioma(List<String> codigoIdioma) {
        switch (codigoIdioma.get(0)) {
            case "es":
                return "Español";
            case "en":
                return "Inglés";
            case "fr":
                return "Francés";
            case "pt":
                return "Portugués";
            default:
                return "Desconocido";
        }
    }
    public DatosGenerales obtenerDatosGeneralesApi() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, DatosGenerales.class);
        return datos;
    }
}
