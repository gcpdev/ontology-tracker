package org.dbpedia.ontologytracker.webservice;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.aksw.rdfunit.io.reader.RdfModelReader;
import org.aksw.rdfunit.io.reader.RdfReader;
import org.aksw.rdfunit.io.reader.RdfReaderException;
import org.aksw.rdfunit.io.reader.RdfReaderFactory;
import org.apache.jena.rdf.model.Model;
import org.dbpedia.ontologytracker.ValidateOntology;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ws")
public class ServiceController {

    @RequestMapping("*")
    public String showInfo() {
        String output =  "Usage: send the ontology file with a POST request to <strong>/ws/ontology</strong> path. " +
                "<br/>The service will return the results in a Turtle format";
        return output;
    }

    @PostMapping(value = {"/ontology"}, consumes = {"text/plain","text/turtle","application/x-turtle"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String inputstream(@RequestBody byte[] file) throws IOException, RdfReaderException {
        try (InputStream is = new ByteArrayInputStream(file)) {
            Model model = ValidateOntology.readOntology(is);
            return ValidateOntology.runTests(model, "TURTLE");
        }
    }

}
