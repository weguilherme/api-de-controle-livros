package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.entities.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long>{
	
//	Usuario findByUsername(String username);
	Optional<Usuario> findByUsername(String username);
	List<Usuario> findBynameContainingIgnoreCase(String name);
}
