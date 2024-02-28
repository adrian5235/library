package com.adrian.library.user;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User get(Integer id);
    List<User> getReaders();
    boolean hasId(Integer id);
    void grantUserRole(Integer id);
    void grantReaderRole(Integer id);
    void grantLibrarianRole(Integer id);
}
