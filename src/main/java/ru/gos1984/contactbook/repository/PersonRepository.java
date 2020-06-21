package ru.gos1984.contactbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.gos1984.contactbook.entity.Person;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
    Page<Person> findAll(Pageable pageble);
    Person findPersonById(Long id);
}
