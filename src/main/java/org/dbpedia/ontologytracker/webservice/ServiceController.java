package org.dbpedia.ontologytracker.webservice;
import java.io.*;

import org.aksw.rdfunit.io.reader.RdfReaderException;
import org.apache.jena.rdf.model.Model;
import org.dbpedia.ontologytracker.ValidateOntology;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws")
public class ServiceController {

    @PostMapping(value = {"/ontology"}, consumes = {"text/plain","text/turtle","application/x-turtle"},
            produces = {
                    "text/turtle",
                    "application/x-turtle",
                    "application/ntriples",
                    "application/n-triples",
                    "application/jsonld",
                    "application/json-ld",
                    "application/json+ld",
                    "text/trig",
                    "text/nquads",
                    "application/rdfxml",
                    "application/rdf-xml",
                    "application/rdf+xml",
                    "application/rdfjson",
                    "application/rdf-json",
                    "application/rdf+json",
                    "text/trix",
                    "application/rdfthrft",
                    "application/rdf+thrift",
                    "application/rdfthrift" })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String inputstream(@RequestBody byte[] file, @RequestHeader(value="Accept") String format) throws IOException, RdfReaderException {

        switch(format) {
            case "text/turtle":
            case "application/x-turtle":
                format = "TURTLE";
                break;
            case "application/ntriples":
            case "application/n-triples":
                format = "NTRIPLES";
                break;
            case "application/jsonld":
            case "application/json-ld":
            case "application/json+ld":
                format = "JSONLD";
                break;
            case "text/trig":
                format = "TRIG";
                break;
            case "text/nquads":
                format = "NQUADS";
                break;
            case "application/rdfxml":
            case "application/rdf-xml":
            case "application/rdf+xml":
                format = "RDFXML";
                break;
            case "application/rdfjson":
            case "application/rdf-json":
            case "application/rdf+json":
                format = "RDFJSON";
                break;
            case "text/trix":
                format = "TRIX";
                break;
            case "application/rdfthrft":
            case "application/rdf+thrift":
            case "application/rdfthrift":
                format = "RDFTHRIFT";
                break;
            default:
                format = "TURTLE";
        }

        try (InputStream is = new ByteArrayInputStream(file)) {
            Model model = ValidateOntology.readOntology(is);
            return ValidateOntology.runTests(model, format);
        }
    }

}
