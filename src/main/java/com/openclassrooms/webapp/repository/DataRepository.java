package com.openclassrooms.webapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.DataContainer;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Repository
public class DataRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = Paths.get("data.json").toFile();


    public DataContainer loadData() throws IOException {
        return objectMapper.readValue(file, DataContainer.class);
    }

    public void saveData(DataContainer data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
}
