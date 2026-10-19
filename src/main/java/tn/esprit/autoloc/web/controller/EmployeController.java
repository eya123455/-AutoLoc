package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;
import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;

    @GetMapping
    public List<Employe> retrieveAllEmployes() {
        return employeService.retrieveAllEmployes();
    }

    @PostMapping
    public Employe addEmploye(@RequestBody Employe e) {
        return employeService.addEmploye(e);
    }

    @PutMapping
    public Employe updateEmploye(@RequestBody Employe e) {
        return employeService.updateEmploye(e);
    }

    @GetMapping("/{id}")
    public Employe retrieveEmploye(@PathVariable Long id) {
        return employeService.retrieveEmploye(id);
    }

    @DeleteMapping("/{id}")
    public void removeEmploye(@PathVariable Long id) {
        employeService.removeEmploye(id);
    }
}
