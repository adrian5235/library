package com.adrian.library.binding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BindingServiceImpl implements BindingService {

    private final BindingRepository repository;

    @Override
    public List<Binding> getAll() {
        return repository.findAll();
    }

    @Override
    public Binding create(Binding binding) {
        return repository.save(binding);
    }

    @Override
    public Binding update(Binding binding) {
        return repository.save(binding);
    }
}
