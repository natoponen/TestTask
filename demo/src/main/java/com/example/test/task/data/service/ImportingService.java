package com.example.test.task.data.service;

import java.io.File;

public interface ImportingService {

    Long startImport(File file);

    void importFromXsl(File file, Long id);

}
