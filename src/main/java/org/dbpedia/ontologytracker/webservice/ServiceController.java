package org.dbpedia.ontologytracker.webservice;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.aksw.rdfunit.io.reader.RdfModelReader;
import org.aksw.rdfunit.io.reader.RdfReader;
import org.aksw.rdfunit.io.reader.RdfReaderException;
import org.aksw.rdfunit.io.reader.RdfReaderFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ws")
public class ServiceController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    //private static Logger log = LoggerFactory.getLogger(ServiceController.class);
    private static final String baseUri = "http://dbpedia.org/ontology/";

    @RequestMapping("/greeting")
    public String doGreet() {
        String output =  "Hello Stranger, the time is "+ new Date();
        return output; //.status(200).entity(output).build();
    }


    @PostMapping(value = "/ontology", produces = {"application/rdf+xml"})
    public RdfReader inputstream(@RequestParam("file") MultipartFile file) throws IOException, RdfReaderException {
        InputStream is = file.getInputStream();
        return new RdfModelReader(
                   RdfReaderFactory.createResourceReader(convertStreamToString(is)).read());
    }

    /*private static void writeResults(final TestExecution testExecution, HttpServletResponse httpServletResponse) throws RdfWriterException, IOException {
        SerializationFormat serializationFormat = configuration.geFirstOutputFormat();
        if (serializationFormat == null) {
            throw new RdfWriterException("Invalid output format");
        }

        httpServletResponse.setContentType(serializationFormat.getHeaderType());
        RdfWriter rdfWriter = RdfResultsWriterFactory.createWriterFromFormat(httpServletResponse.getOutputStream(), serializationFormat, testExecution);
        rdfWriter.write(ModelFactory.createDefaultModel());
    }*/


    private static String convertStreamToString(java.io.InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] byteArray = buffer.toByteArray();

        String text = new String(byteArray, StandardCharsets.UTF_8);
        return text;
    }

}
