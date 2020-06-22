package ru.gos1984.contactbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.PersonRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class AppController {

    @Value("${spring.app.source}")
    private String appSource;

    private PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "order", defaultValue = "asc") String order,
                        @RequestParam(value = "search", defaultValue = "") String search,
                        @RequestParam(value = "theme", defaultValue = "") String theme,
                        @RequestParam(value = "sort", defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.fromString(order.equals("desc") ? "desc" : "asc"), sort));
        Page<Person> persons;
        if(search.isEmpty() && theme.isEmpty()) {
            persons = personRepository.findAll(pageable);
        } else {
            switch(theme) {
                case "lastName":
                    persons = personRepository.findAllByLastNameIgnoreCaseContains(search, pageable);
                    break;
                case "phone":
                    persons = personRepository.findAllByPhoneIgnoreCaseContains(search, pageable);
                    break;
                case "email":
                    persons = personRepository.findAllByEmailIgnoreCaseContains(search, pageable);
                    break;
                default:
                    persons = personRepository.findAllByFirstNameIgnoreCaseContains(search, pageable);
            }
        }
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
                             @RequestParam Map<String, String> param,
                             @RequestParam("avatar") MultipartFile avatar) {
        Long id = Long.parseLong(param.get("id"));
        Person person = personRepository.findPersonById(id);

        if(!avatar.isEmpty()) {
            File file = new File(appSource + avatar.getOriginalFilename());
            try(FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(avatar.getBytes());
                person.setAvatar("/img/" + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
