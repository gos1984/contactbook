package ru.gos1984.contactbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.PersonRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AppController {

    PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "sort", defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Person> persons = personRepository.findAll(pageable);
        model.addAttribute("persons", persons);
        return "person/index";
    }

    @GetMapping("/person/edit/{id}")
    public String edit(Model model,
                        @PathVariable("id") Long id) {
        Person person = personRepository.findPersonById(id);
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PostMapping("/person/edit")
    public String editPerson(Model model,
                             @RequestParam Map<String, String> param) {
        Long id = Long.parseLong(param.get("id"));
        Person person = personRepository.findPersonById(id);
        person.setFirstName(param.get("firstName"));
        person.setLastName(param.get("lastName"));
        person.setPhone(param.get("phone"));
        person.setEmail(param.get("email"));
        personRepository.save(person);
        return "redirect:/person/edit/" + id;
    }

    @GetMapping("/person/delete/{id}")
    public String delete(Model model,
                         @PathVariable("id") Long id) {
        Person person = personRepository.findPersonById(id);
        personRepository.delete(person);
        return "redirect:/";
    }
}
