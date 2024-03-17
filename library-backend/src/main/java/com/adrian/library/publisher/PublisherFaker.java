package com.adrian.library.publisher;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublisherFaker {

    private final PublisherRepository publisherRepository;
    private final Faker faker = new Faker();

    public void savePublishers(int n) {
        List<Publisher> publishers = publisherRepository.findAll();

        for (int i = 0; i < n; i++) {
            String name = faker.book().publisher();
            while (contains(publishers, name)) {
                name = faker.book().publisher();
            }
            Publisher publisher = new Publisher(name);
            publishers.add(publisher);
            publisherRepository.save(publisher);
        }
    }

    // checks if publisher with a given name exists in given list of publishers
    private boolean contains(List<Publisher> publishers, String name) {
        for (Publisher publisher : publishers) {
            if (publisher.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
