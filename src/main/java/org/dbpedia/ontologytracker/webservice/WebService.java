/******************************************************************************/
/*  Copyright (C) 2017, Gustavo Publio                                        */
/*                                                                            */
/*  Licensed under the Apache License, Version 2.0 (the "License");           */
/*  you may not use this file except in compliance with the License.          */
/*  You may obtain a copy of the License at                                   */
/*                                                                            */
/*      http://www.apache.org/licenses/LICENSE-2.0                            */
/*                                                                            */
/*  Unless required by applicable law or agreed to in writing, software       */
/*  distributed under the License is distributed on an "AS IS" BASIS,         */
/*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  */
/*  See the License for the specific language governing permissions and       */
/*  limitations under the License.                                            */
/******************************************************************************/

package org.dbpedia.ontologytracker.webservice;

//import com.hp.hpl.jena.ontology.OntModel;
//import com.hp.hpl.jena.rdf.model.Model;

import org.aksw.rdfunit.model.interfaces.results.ShaclTestCaseResult;
import org.apache.jena.rdf.model.Model;
import org.dbpedia.ontologytracker.ValidateOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

//import com.jamonapi.Monitor;
//import com.jamonapi.MonitorFactory;
//import org.nlp2rdf.core.NIFParameters;
//import org.nlp2rdf.core.RDFUnitValidatorWrapper;

/**
 * User: gcpdev
 * Date: 23.09.17
 */
@Path("/")
public class WebService { //extends HttpServlet {

    /** TODO:
     * - receive parameters (ontology; format?)
     * - run tests on ontology
     * - write tests output
     */


    private static Logger log = LoggerFactory.getLogger(WebService.class);
    private static final String baseUri = "http://dbpedia.org/ontology/";


    @Path("/greet")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response doGreet() {
        String output =  "Hello Stranger, the time is "+ new Date();
        return Response.status(200).entity(output).build();
    }


    @Path("/ontology")
    @Consumes("text/turtle")
    @POST
    @Produces("application/rdf+xml")
    public void inputstream(final InputStream is) throws IOException {
        Model ont = ValidateOntology.readOntology(is);
        Collection<ShaclTestCaseResult> tcrs = ValidateOntology.runTests(ont);
        tcrs.stream().forEach(t -> {
            t.getResultAnnotations();
        });
    }




}