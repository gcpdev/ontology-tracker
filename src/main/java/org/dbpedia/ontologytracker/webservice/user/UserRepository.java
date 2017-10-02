package org.dbpedia.ontologytracker.webservice.user;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<WebServiceUser, String> {

    public WebServiceUser findByUsername(String username);
    //public List<ShaclTest> findUserTests(String userName);

}