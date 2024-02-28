package com.adrian.library.publisher;

import java.util.List;

public interface PublisherService {
    List<Publisher> getAll();
    Publisher create(Publisher publisher);
    Publisher update(Publisher publisher);
}
