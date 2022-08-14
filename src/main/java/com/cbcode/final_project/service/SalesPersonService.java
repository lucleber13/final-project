package com.cbcode.final_project.service;

import com.cbcode.final_project.domain.SalesPerson;
import com.cbcode.final_project.domain.User;
import com.cbcode.final_project.model.SalesPersonDTO;
import com.cbcode.final_project.repos.SalesPersonRepository;
import com.cbcode.final_project.repos.UserRepository;
import com.cbcode.final_project.util.WebUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class SalesPersonService {

    private final SalesPersonRepository salesPersonRepository;
    private final UserRepository userRepository;

    public SalesPersonService(final SalesPersonRepository salesPersonRepository,
            final UserRepository userRepository) {
        this.salesPersonRepository = salesPersonRepository;
        this.userRepository = userRepository;
    }

    public List<SalesPersonDTO> findAll() {
        return salesPersonRepository.findAll(Sort.by("id"))
                .stream()
                .map(salesPerson -> mapToDTO(salesPerson, new SalesPersonDTO()))
                .collect(Collectors.toList());
    }

    public SalesPersonDTO get(final Long id) {
        return salesPersonRepository.findById(id)
                .map(salesPerson -> mapToDTO(salesPerson, new SalesPersonDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final SalesPersonDTO salesPersonDTO) {
        final SalesPerson salesPerson = new SalesPerson();
        mapToEntity(salesPersonDTO, salesPerson);
        return salesPersonRepository.save(salesPerson).getId();
    }

    public void update(final Long id, final SalesPersonDTO salesPersonDTO) {
        final SalesPerson salesPerson = salesPersonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(salesPersonDTO, salesPerson);
        salesPersonRepository.save(salesPerson);
    }

    public void delete(final Long id) {
        salesPersonRepository.deleteById(id);
    }

    private SalesPersonDTO mapToDTO(final SalesPerson salesPerson,
            final SalesPersonDTO salesPersonDTO) {
        salesPersonDTO.setId(salesPerson.getId());
        salesPersonDTO.setName(salesPerson.getName());
        salesPersonDTO.setUser(salesPerson.getUser() == null ? null : salesPerson.getUser().getId());
        return salesPersonDTO;
    }

    private SalesPerson mapToEntity(final SalesPersonDTO salesPersonDTO,
            final SalesPerson salesPerson) {
        salesPerson.setName(salesPersonDTO.getName());
        final User user = salesPersonDTO.getUser() == null ? null : userRepository.findById(salesPersonDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        salesPerson.setUser(user);
        return salesPerson;
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final SalesPerson salesPerson = salesPersonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!salesPerson.getSalesCars().isEmpty()) {
            return WebUtils.getMessage("salesPerson.car.manyToOne.referenced", salesPerson.getSalesCars().iterator().next().getId());
        }
        return null;
    }

}
