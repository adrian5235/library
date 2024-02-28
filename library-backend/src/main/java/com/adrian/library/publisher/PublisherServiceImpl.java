package com.adrian.library.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository repository;

    @Override
    public List<Publisher> getAll() {
        return repository.findAll();
    }

    @Override
    public Publisher create(Publisher publisher) {
        return repository.save(publisher);
    }

    @Override
    public Publisher update(Publisher publisher) {
        return repository.save(publisher);
    }
}
