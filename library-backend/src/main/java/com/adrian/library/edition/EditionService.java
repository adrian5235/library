package com.adrian.library.edition;

import java.util.List;

public interface EditionService {
    List<Edition> getAll();
    List<Edition> getOnePerBook();
    List<Edition> getByBookId(Integer bookId);
    Edition getByCopyId(Integer copyId);
    Edition get(Integer id);
    Edition create(Edition copy);
    Edition update(Edition copy);
    void delete(Integer id);
}
