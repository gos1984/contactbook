package ru.gos1984.contactbook.component;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.gos1984.contactbook.entity.Company;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.CompanyRepository;
import ru.gos1984.contactbook.repository.PersonRepository;

@Component
public class Generate implements ApplicationRunner {

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        personRepository.deleteAll();

        Faker faker = new Faker();
        for(int i = 0; i < 10; i++) {
            Company company = new Company(
                    faker.company().name(),
                    faker.company().logo(),
                    faker.company().industry(),
                    faker.address().fullAddress(),
                    faker.phoneNumber().cellPhone()
            );
            companyRepository.save(company);
            for(int j = 0; j < 10; j++) {
                Person person = new Person(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.phoneNumber().cellPhone(),
                        faker.internet().emailAddress(),
                        faker.avatar().image()
                );
                company.addPerson(person);
                personRepository.save(person);
            }

        }
    }
}
