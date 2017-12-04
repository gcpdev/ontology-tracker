package org.dbpedia.ontologytracker.webservice;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.aksw.rdfunit.io.reader.RdfReaderException;
import org.apache.jena.rdf.model.Model;
import org.dbpedia.ontologytracker.ValidateOntology;
import org.dbpedia.ontologytracker.webservice.test.ShaclTest;
import org.dbpedia.ontologytracker.webservice.test.ShaclTestRepository;
import org.dbpedia.ontologytracker.webservice.user.UserRepository;
import org.dbpedia.ontologytracker.webservice.user.WebServiceUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
//@SpringBootApplication
@RestController
@RequestMapping("/ws")
public class ServiceController extends Exception {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShaclTestRepository shaclTestRepository;
	private static Logger L = LoggerFactory.getLogger(ServiceController.class);
	@Autowired
	private ApplicationContext applicationContext;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping(value = {"/ontology"}, consumes = {"text/plain","text/turtle","application/x-turtle"},
            produces = {
                    "text/plain", //for returning errors strings
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
    public String ontology(@RequestBody byte[] file, @RequestHeader(value="Accept") String format) throws IOException, RdfReaderException {

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

    @PostMapping(value = {"/testOntology"}, consumes = {"text/plain","text/turtle","application/x-turtle"},
            produces = {
                    "text/plain", //for returning errors strings
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
    public String testOntology(@RequestBody byte[] file, @RequestHeader(value="Accept") String format, @RequestHeader(value="Test") int testNumber, Authentication authentication) throws IOException, RdfReaderException {

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

            String authUser = authentication.getName();

            WebServiceUser user = userRepository.findByUsername(authUser);

            ShaclTest shacl = shaclTestRepository.findByUserAndIdFromThisUser(user, testNumber);

            return ValidateOntology.runTests(model, format, shacl.getTest());
        }
    }

    // added by @asanchez75 for sending an URL ontology
    @PostMapping(value = {"/ontologyURL"}, consumes = {"text/plain"},
            produces = {
                    "text/plain", //for returning errors strings
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
    public String ontologyURL(@RequestBody String url, @RequestHeader(value="Accept") String format) throws IOException, RdfReaderException {

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
    	    Resource resource = applicationContext.getResource(url);
        try (InputStream is = resource.getInputStream()) {        	
            Model model = ValidateOntology.readOntology(is);
            return ValidateOntology.runTests(model, format);
        }
    }
    
    // added by @asanchez75 for uploading two files
    @RequestMapping(value = "/ontologyUpload", method = RequestMethod.POST, consumes = {"multipart/form-data"},  headers = {"content-type=multipart/mixed","content-type=multipart/form-data"})    
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String ontologyUpload(@RequestPart(value="ontology") MultipartFile ontology, @RequestParam(value="format") String format, Authentication authentication) throws IOException, RdfReaderException {
    	 L.info("Multiple file upload!");
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

        try (InputStream is = ontology.getInputStream()) {        	
            Model model = ValidateOntology.readOntology(is);
            return ValidateOntology.runTests(model, format);
        }
 
    }
}
