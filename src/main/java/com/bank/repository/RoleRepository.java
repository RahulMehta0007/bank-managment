package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.model.ERole;
import com.bank.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	Optional<Role> findByName(ERole name);
}
