package com.deepaksaini.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.deepaksaini.repository.LibraryRepository;
import com.deepaksaini.service.LibraryService;

@RestController
public class LibraryController {
	@Autowired
	LibraryRepository repository;

	@Autowired
	LibraryService libraryService;

	AddResponse add =new AddResponse();
	HttpHeaders header= new HttpHeaders();
	
	private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);


	@SuppressWarnings("rawtypes")
	@PostMapping("/addBook")
	public  ResponseEntity addBookImplementation(@RequestBody Library library) {

		//add book details into database
		//jpa repository-- save()

		String id =libraryService.buildId(library.getIsbn(), library.getAisle());// dependency mock

		if(!libraryService.checkBookAlreadyExit(id)) {// dependency mock
			
			logger.info("Book do not exist so creating one");

			library.setId(id);
			repository.save(library);// dependency mock


			add.setMsg("Success Book is Added");
			add.setId(id);

			header.add("unique", id);
			return new ResponseEntity<AddResponse>(add,header,HttpStatus.CREATED);

		}
		else {
			logger.info("Book exist so skipping creation");
			//write a code to tell the book ia already exit
			add.setMsg("Book already exist");
			add.setId(id);
			return new ResponseEntity<AddResponse>(add,HttpStatus.ACCEPTED);
		}

	}


	@GetMapping("/getBooks/{id}")
	public Library getBookById(@PathVariable(value="id") String id) {

		try {
			Library lib=repository.findById(id).get();
			return lib;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	} 

	@GetMapping("/getBooks/author")
	public List<Library> getBookByAuthorName(@RequestParam(value="authorname") String authorname) {

		return repository.findAllAuthor(authorname);
	} 


	@PutMapping("/updateBook/{id}")
	public ResponseEntity<Library> updateBook(@PathVariable(value="id") String id, @RequestBody Library library) {

		//Library existingBook =repository.findById(id).get();
		Library existingBook = libraryService.getBookById(id);
		existingBook.setAisle(library.getAisle());
		existingBook.setAuthor(library.getAuthor());
		existingBook.setBook_name(library.getBook_name());
		repository.save(existingBook);
		return new ResponseEntity<Library>(existingBook, HttpStatus.OK);

	}

	@DeleteMapping("/deleteBook")
	public ResponseEntity<String> deleteBokkById(@RequestBody Library library)
	{
		try {
			//Library libDelete=repository.findById(library.getId()).get();
			Library libDelete = libraryService.getBookById(library.getId());
			repository.delete(libDelete);
			logger.info("Book is deleted");
			return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}

