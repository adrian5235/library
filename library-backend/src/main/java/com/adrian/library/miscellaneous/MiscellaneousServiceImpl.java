package com.adrian.library.miscellaneous;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MiscellaneousServiceImpl implements MiscellaneousService {

    private final MiscellaneousRepository repository;

    @Override
    public List<Miscellaneous> get() {
        return repository.findAll();
    }

    @Override
    public Miscellaneous update(Miscellaneous miscellaneous) {
        return repository.save(miscellaneous);
    }
}
