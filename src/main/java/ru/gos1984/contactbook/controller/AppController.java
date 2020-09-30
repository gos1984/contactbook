package ru.gos1984.contactbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.PersonRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order.equals("desc") ? "desc" : "asc"), sort));
        Page<Person> persons;
        if (search.isEmpty() && theme.isEmpty()) {
            persons = personRepository.findAll(pageable);
        } else {
            switch (theme) {
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

    @PostMapping("/person/edit/full")
    public String editFullPerson(Model model,
                                 @RequestParam Map<String, String> param,
                                 @RequestParam(name = "avatar", required = false, defaultValue = "") MultipartFile avatar) {
        System.out.println(avatar);
        Long id = Long.parseLong(param.get("id"));
        Person person = personRepository.findPersonById(id);

        File file = new File(appSource + avatar.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(avatar.getBytes());
            person.setAvatar("/img/" + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        personRepository.save(changeDataPerson(person,
                param.get("firstName"),
                param.get("lastName"),
                param.get("phone"),
                param.get("email"))
        );
        return "redirect:/person/edit/" + id;
    }

    @PostMapping("/person/edit/part")
    public String editPartPerson(Model model, @RequestParam Map<String, String> param) {
        Long id = Long.parseLong(param.get("id"));

        personRepository.save(changeDataPerson(
                personRepository.findPersonById(id),
                param.get("firstName"),
                param.get("lastName"),
                param.get("phone"),
                param.get("email"))
        );
        return "redirect:/person/edit/" + id;
    }

    @GetMapping("/person/delete/{id}")
    public String delete(Model model,
                         @PathVariable("id") Long id) {
        Person person = personRepository.findPersonById(id);
        personRepository.delete(person);
        return "redirect:/";
    }

    private Person changeDataPerson(Person person, String firstName, String lastName, String phone, String email) {
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setPhone(phone);
        person.setEmail(email);
        return person;
    }
}
