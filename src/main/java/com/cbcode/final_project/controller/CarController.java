package com.cbcode.final_project.controller;

import com.cbcode.final_project.domain.SalesPerson;
import com.cbcode.final_project.model.CarDTO;
import com.cbcode.final_project.repos.SalesPersonRepository;
import com.cbcode.final_project.service.CarService;
import com.cbcode.final_project.util.WebUtils;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final SalesPersonRepository salesPersonRepository;

    public CarController(final CarService carService,
            final SalesPersonRepository salesPersonRepository) {
        this.carService = carService;
        this.salesPersonRepository = salesPersonRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("salesValues", salesPersonRepository.findAll().stream().collect(
                Collectors.toMap(SalesPerson::getId, SalesPerson::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cars", carService.findAll());
        return "car/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("car") final CarDTO carDTO) {
        return "car/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("car") @Valid final CarDTO carDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("regNumber") && carService.regNumberExists(carDTO.getRegNumber())) {
            bindingResult.rejectValue("regNumber", "Exists.car.regNumber");
        }
        if (!bindingResult.hasFieldErrors("keysNumber") && carService.keysNumberExists(carDTO.getKeysNumber())) {
            bindingResult.rejectValue("keysNumber", "Exists.car.keysNumber");
        }
        if (bindingResult.hasErrors()) {
            return "car/add";
        }
        carService.create(carDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("car.create.success"));
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("car", carService.get(id));
        return "car/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("car") @Valid final CarDTO carDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("regNumber") &&
                !carService.get(id).getRegNumber().equalsIgnoreCase(carDTO.getRegNumber()) &&
                carService.regNumberExists(carDTO.getRegNumber())) {
            bindingResult.rejectValue("regNumber", "Exists.car.regNumber");
        }
        if (!bindingResult.hasFieldErrors("keysNumber") &&
                !carService.get(id).getKeysNumber().equalsIgnoreCase(carDTO.getKeysNumber()) &&
                carService.keysNumberExists(carDTO.getKeysNumber())) {
            bindingResult.rejectValue("keysNumber", "Exists.car.keysNumber");
        }
        if (bindingResult.hasErrors()) {
            return "car/edit";
        }
        carService.update(id, carDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("car.update.success"));
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        carService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("car.delete.success"));
        return "redirect:/cars";
    }

}
