package dev.stunning.userservice.Repositories;

import dev.stunning.userservice.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
