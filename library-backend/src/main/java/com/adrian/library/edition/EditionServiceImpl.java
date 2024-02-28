package com.adrian.library.edition;

import com.adrian.library.book.Book;
import com.adrian.library.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EditionServiceImpl implements EditionService {

    private final EditionRepository editionRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Edition> getAll() {
        return editionRepository.findAll();
    }

    @Override
    public List<Edition> getOnePerBook() {
        List<Book> books = bookRepository.findAll();
        List<Edition> editions = new ArrayList<>();

        for(Book book : books) {
            List<Edition> bookEditions = book.getEditions();
            if (!bookEditions.isEmpty()) {
                boolean imageFound = false;
                List<Edition> availableEditions = new ArrayList<>();
                for (Edition edition : bookEditions) {
                    if (edition.getQuantity() > 0) {
                        if (edition.getImage() != null) {
                            imageFound = true;
                            editions.add(edition);
                            break;
                        } else {
                            availableEditions.add(edition);
                        }
                    }
                }
                if (!imageFound) {
                    if (!availableEditions.isEmpty()) {
                        editions.add(availableEditions.get(0));
                    } else {
                        editions.add(bookEditions.get(0));
                    }
                }
            }
        }
        return editions;
    }

    @Override
    public List<Edition> getByBookId(Integer bookId) {
        return editionRepository.findByBookId(bookId);
    }

    @Override
    public Edition getByCopyId(Integer copyId) {

        return editionRepository.findByCopyId(copyId);
    }

    @Override
    public Edition get(Integer id) {
        return editionRepository.getReferenceById(id);
    }

    @Override
    public Edition create(Edition edition) {
        return editionRepository.save(edition);
    }

    @Override
    public Edition update(Edition edition) {
        return editionRepository.save(edition);
    }

    @Override
    public void delete(Integer id) {
        editionRepository.deleteById(id);
    }
}
