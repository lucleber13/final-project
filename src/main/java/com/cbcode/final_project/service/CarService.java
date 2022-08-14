package com.cbcode.final_project.service;

import com.cbcode.final_project.domain.Car;
import com.cbcode.final_project.domain.SalesPerson;
import com.cbcode.final_project.model.CarDTO;
import com.cbcode.final_project.repos.CarRepository;
import com.cbcode.final_project.repos.SalesPersonRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CarService {

    private final CarRepository carRepository;
    private final SalesPersonRepository salesPersonRepository;

    public CarService(final CarRepository carRepository,
            final SalesPersonRepository salesPersonRepository) {
        this.carRepository = carRepository;
        this.salesPersonRepository = salesPersonRepository;
    }

    public List<CarDTO> findAll() {
        return carRepository.findAll(Sort.by("id"))
                .stream()
                .map(car -> mapToDTO(car, new CarDTO()))
                .collect(Collectors.toList());
    }

    public CarDTO get(final Long id) {
        return carRepository.findById(id)
                .map(car -> mapToDTO(car, new CarDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final CarDTO carDTO) {
        final Car car = new Car();
        mapToEntity(carDTO, car);
        return carRepository.save(car).getId();
    }

    public void update(final Long id, final CarDTO carDTO) {
        final Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(carDTO, car);
        carRepository.save(car);
    }

    public void delete(final Long id) {
        carRepository.deleteById(id);
    }

    private CarDTO mapToDTO(final Car car, final CarDTO carDTO) {
        carDTO.setId(car.getId());
        carDTO.setModel(car.getModel());
        carDTO.setColor(car.getColor());
        carDTO.setRegNumber(car.getRegNumber());
        carDTO.setKeysNumber(car.getKeysNumber());
        carDTO.setSales(car.getSales() == null ? null : car.getSales().getId());
        return carDTO;
    }

    private Car mapToEntity(final CarDTO carDTO, final Car car) {
        car.setModel(carDTO.getModel());
        car.setColor(carDTO.getColor());
        car.setRegNumber(carDTO.getRegNumber());
        car.setKeysNumber(carDTO.getKeysNumber());
        final SalesPerson sales = carDTO.getSales() == null ? null : salesPersonRepository.findById(carDTO.getSales())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sales not found"));
        car.setSales(sales);
        return car;
    }

    public boolean regNumberExists(final String regNumber) {
        return carRepository.existsByRegNumberIgnoreCase(regNumber);
    }

    public boolean keysNumberExists(final String keysNumber) {
        return carRepository.existsByKeysNumberIgnoreCase(keysNumber);
    }

}
