package org.example.ioinstruments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

public class InputParser {
    public void parseInput() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            String userDirectory = FileSystems.getDefault()
                    .getPath("")
                    .toAbsolutePath()
                    .toString();
            System.out.println("dir is \t" + userDirectory);
            String filePath = userDirectory + "\\input_data" + "\\simple_input.txt";
            System.out.println("file path is \t" + filePath);
//            FileInputStream inputStream = new FileInputStream(filePath);
//            String everything = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//            System.out.println(everything);
            File jsonInputFile = new File(filePath);
            Person person = objectMapper.readValue(jsonInputFile, Person.class);
            System.out.println("age is " + person.getAge());
            System.out.println("name is " + person.getFirstName());
            System.out.println("last name is " + person.getLastName());

        } catch (IOException e) {
            System.out.println("failed to read file");
        }
    }
}
