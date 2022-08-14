package com.cbcode.final_project.controller;

import com.cbcode.final_project.domain.User;
import com.cbcode.final_project.model.SalesPersonDTO;
import com.cbcode.final_project.repos.UserRepository;
import com.cbcode.final_project.service.SalesPersonService;
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
@RequestMapping("/salesPersons")
public class SalesPersonController {

    private final SalesPersonService salesPersonService;
    private final UserRepository userRepository;

    public SalesPersonController(final SalesPersonService salesPersonService,
            final UserRepository userRepository) {
        this.salesPersonService = salesPersonService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll().stream().collect(
                Collectors.toMap(User::getId, User::getUserName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("salesPersons", salesPersonService.findAll());
        return "salesPerson/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("salesPerson") final SalesPersonDTO salesPersonDTO) {
        return "salesPerson/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("salesPerson") @Valid final SalesPersonDTO salesPersonDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "salesPerson/add";
        }
        salesPersonService.create(salesPersonDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("salesPerson.create.success"));
        return "redirect:/salesPersons";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("salesPerson", salesPersonService.get(id));
        return "salesPerson/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("salesPerson") @Valid final SalesPersonDTO salesPersonDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "salesPerson/edit";
        }
        salesPersonService.update(id, salesPersonDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("salesPerson.update.success"));
        return "redirect:/salesPersons";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = salesPersonService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            salesPersonService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("salesPerson.delete.success"));
        }
        return "redirect:/salesPersons";
    }

}
