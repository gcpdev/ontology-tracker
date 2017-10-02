package org.dbpedia.ontologytracker.webservice;

import org.springframework.data.annotation.Id;

public class ShaclTest {

    @Id
    public String id;

    private String test;
    private String owner;

    public ShaclTest() {}

    public ShaclTest(String test, String owner) {
        this.setTest(test);
        this.setOwner(owner);
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ShaclTest getShaclTest(String id) {
        return new ShaclTest(test, owner);
    }
}
