package com.deepaksaini;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.deepaksaini.controller.AddResponse;
import com.deepaksaini.controller.Library;
import com.deepaksaini.controller.LibraryController;
import com.deepaksaini.repository.LibraryRepository;
import com.deepaksaini.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootRestServiceApplicationTests {

	@Autowired
	LibraryService lib;

	@Autowired
	LibraryController libController;

	@MockBean
	LibraryRepository repository;

	@MockBean
	LibraryService libraryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test()
	public void checkbuildIdLogic() {

		String id = lib.buildId("Ziology", 243);
		assertEquals(id, "OLDZiology243");

		String id1 = lib.buildId("Biology", 111);
		assertEquals(id1, "Man111");

	}

	@Test
	public void addBookTest() {

		// mock
		Library lib = buildLibrary();
		when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
		when(libraryService.checkBookAlreadyExit(lib.getId())).thenReturn(true);
		when(repository.save(any())).thenReturn(lib);

		ResponseEntity response = libController.addBookImplementation(buildLibrary());
		System.out.println(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
		AddResponse ad = (AddResponse) response.getBody();
		ad.getId();
		assertEquals(lib.getId(), ad.getId());

		ad.getMsg();
		assertEquals("Book already exist", ad.getMsg());

	}

	// call Mock service from code

	@Test
	public void addBookControllerTest() throws Exception {

		// mock
		Library lib = buildLibrary();

		// convert java object into json
		ObjectMapper map = new ObjectMapper();
		String jsonString = map.writeValueAsString(lib);

		when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
		when(libraryService.checkBookAlreadyExit(lib.getId())).thenReturn(false);
		when(repository.save(any())).thenReturn(lib);

		this.mockMvc.perform(post("/addBook").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(lib.getId()));

	}

	@Test
	public void getBookByAuthorNameTest() throws Exception {

		List<Library> li = new ArrayList<Library>();
		li.add(buildLibrary());
		li.add(buildLibrary());
		when(repository.findAllAuthor(any())).thenReturn(li);

		this.mockMvc.perform(get("/getBooks/author").param("authorname", "Neha ")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].id").value("fgh389"));

	}

	@Test
	public void updateBookTest() throws Exception {

		Library lib = buildLibrary();
		ObjectMapper map = new ObjectMapper();
		String jsonString = map.writeValueAsString(updateLibrary());

		when(libraryService.getBookById(any())).thenReturn(buildLibrary());

		this.mockMvc
				.perform(put("/updateBook/" + lib.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().json(
						"{\"book_name\":\"Angular\",\"id\":\"fgh389\",\"isbn\":\"fgh\",\"aisle\":300,\"author\":\"Sneha\"}"));
	}

	@Test
	public void deleteBookControllerTest() throws Exception {

		when(libraryService.getBookById(any())).thenReturn(buildLibrary());

		// doNothing is used when controller method return type is void
		doNothing().when(repository).delete(buildLibrary());

		this.mockMvc
				.perform(delete("/deleteBook").contentType(MediaType.APPLICATION_JSON).content("{\"id\" : \"fgh389\"}"))
				.andDo(print()).andExpect(status().isCreated()).andExpect(content().string("Book is deleted"));
	}

	public Library buildLibrary() {
		Library library = new Library();
		library.setAisle(389);
		library.setAuthor("Neha");
		library.setBook_name("Core java");
		library.setIsbn("fgh");
		library.setId("fgh389");
		return library;

	}

	public Library updateLibrary() {
		Library library = new Library();
		library.setAisle(300);
		library.setAuthor("Sneha");
		library.setBook_name("Angular");
		library.setIsbn("rani");
		library.setId("rani300");
		return library;

	}

}
