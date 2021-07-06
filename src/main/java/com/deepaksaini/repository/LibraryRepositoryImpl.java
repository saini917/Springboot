package com.deepaksaini.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.deepaksaini.controller.Library;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom{

	@Autowired
	LibraryRepository repository;

	@Override
	public List<Library> findAllAuthor(String authorName) {
		
		List<Library> bookswithAuthor=new ArrayList<Library>();

		List<Library> books=repository.findAll();
		for(Library item : books) {
			if(item.getAuthor().equalsIgnoreCase(authorName))
			{
				bookswithAuthor.add(item);
			}
		}

		return bookswithAuthor;
	}
}


