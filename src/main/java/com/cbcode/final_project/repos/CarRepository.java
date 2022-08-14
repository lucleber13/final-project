package com.cbcode.final_project.repos;

import com.cbcode.final_project.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByRegNumberIgnoreCase(String regNumber);

    boolean existsByKeysNumberIgnoreCase(String keysNumber);

}
