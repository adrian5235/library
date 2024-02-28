package com.adrian.library.binding;

import java.util.List;

public interface BindingService {
    List<Binding> getAll();
    Binding create(Binding binding);
    Binding update(Binding binding);
}
