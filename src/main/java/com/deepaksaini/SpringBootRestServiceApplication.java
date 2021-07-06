package com.deepaksaini;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.deepaksaini.controller.Library;
import com.deepaksaini.repository.LibraryRepository;

@SpringBootApplication
public class SpringBootRestServiceApplication {//implements CommandLineRunner {

	@Autowired
	LibraryRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestServiceApplication.class, args);
	}

	// @Override	
	/*
	 * public void run(String[] args) {
	 * 
	 * Library library=repository.findById("App123").get();
	 * System.out.println(library.getAuthor());
	 * 
	 * 
	 * Library lib = new Library(); lib.setAuthor("Siya");
	 * lib.setBook_name("Devops"); lib.setAisle(456); lib.setIsbn("uijh");
	 * lib.setId("uijh456");
	 * 
	 * repository.save(lib);
	 * 
	 * 
	 * List<Library> allrecords=repository.findAll(); for(Library item: allrecords)
	 * { System.out.println(item.getBook_name()); }
	 * 
	 * repository.delete(lib);
	 * 
	 * 
	 * }
	 */

}
