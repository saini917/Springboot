package com.deepaksaini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deepaksaini.controller.Library;

//JpaRepository asked for two arguments one is bean class and second is primary return type

@Repository
public interface LibraryRepository extends JpaRepository<Library, String>,LibraryRepositoryCustom {

}
