package ru.gos1984.contactbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.gos1984.contactbook.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findAll(Pageable pageble);
    Person findPersonById(Long id);
    Page<Person> findAllByFirstNameIgnoreCaseContains(String firstName, Pageable pageble);
    Page<Person> findAllByLastNameIgnoreCaseContains(String firstName, Pageable pageble);
    Page<Person> findAllByPhoneIgnoreCaseContains(String firstName, Pageable pageble);
    Page<Person> findAllByEmailIgnoreCaseContains(String firstName, Pageable pageble);
}
