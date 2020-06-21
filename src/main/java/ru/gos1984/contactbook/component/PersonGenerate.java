package ru.gos1984.contactbook.component;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.gos1984.contactbook.entity.Person;
import ru.gos1984.contactbook.repository.PersonRepository;

@Component
public class PersonGenerate implements ApplicationRunner {

    PersonRepository personRepository;

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        personRepository.deleteAll();

        Faker faker = new Faker();
        for(int i = 0; i < 1000; i++) {
            Person person = new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.phoneNumber().cellPhone(),
                    faker.internet().emailAddress(),
                    faker.avatar().image()
            );
            personRepository.save(person);
        }
    }
}
