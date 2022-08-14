package com.cbcode.final_project.repos;

import com.cbcode.final_project.domain.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SalesPersonRepository extends JpaRepository<SalesPerson, Long> {
}
