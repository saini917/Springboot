package com.deepaksaini.repository;

import java.util.List;
import com.deepaksaini.controller.Library;

public interface LibraryRepositoryCustom {
	
	List<Library> findAllAuthor(String authorName);

}
