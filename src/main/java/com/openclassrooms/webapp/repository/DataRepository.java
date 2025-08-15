package com.openclassrooms.webapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.DataContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;

@Repository
public class DataRepository {

    private final ObjectMapper objectMapper;
    private final File file;

    public DataRepository(ObjectMapper objectMapper, @Value("${data.file.path}") String filePath) {
        this.objectMapper = objectMapper;
        this.file = new File(filePath);
    }

    public DataContainer loadData() throws IOException {
        return objectMapper.readValue(file, DataContainer.class);
    }

    public void saveData(DataContainer data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
}
