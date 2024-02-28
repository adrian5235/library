package com.adrian.library.user;

import com.adrian.library.loan.LoanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User get(Integer id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<User> getReaders() {
        return repository.findUsersByRole(Role.READER);
    }

    // used to authorize user by making sure his id equals id of a user whom data he is trying to access
    @Override
    public boolean hasId(Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = repository.findByEmail(username);
        return user.get().getId() == id;
    }

    @Override
    public void grantUserRole(Integer id) {
        User user = repository.getReferenceById(id);
        user.setRole(Role.USER);
        repository.save(user);
    }

    @Override
    public void grantReaderRole(Integer id) {
        User user = repository.getReferenceById(id);
        user.setRole(Role.READER);
        repository.save(user);
    }

    @Override
    public void grantLibrarianRole(Integer id) {
        User user = repository.getReferenceById(id);
        user.setRole(Role.LIBRARIAN);
        repository.save(user);
    }
}
