package ru.gos1984.contactbook.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.gos1984.contactbook.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyById(Long id);
}
