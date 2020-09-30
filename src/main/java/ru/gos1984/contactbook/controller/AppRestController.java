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
import ru.gos1984.contactbook.entity.Company;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.CompanyRepository;
import ru.gos1984.contactbook.repository.PersonRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class AppRestController {

    private PersonRepository personRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/companies")
    public Page<Company> companies(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        return companyRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order.equals("desc") ? "desc" : "asc"), sort)));
    }

    @GetMapping("/persons")
    public Page<Person> persons(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        return personRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order.equals("desc") ? "desc" : "asc"), sort)));
    }
}
