package com.adrian.library.miscellaneous;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/miscellaneous")
public class MiscellaneousController {

    private final MiscellaneousServiceImpl service;

    @GetMapping
    List<Miscellaneous> get() {
        return service.get();
    }

    @PutMapping
    Miscellaneous update(@RequestBody Miscellaneous miscellaneous) {
        return service.update(miscellaneous);
    }
}
