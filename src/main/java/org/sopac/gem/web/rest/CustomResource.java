package org.sopac.gem.web.rest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopac.gem.domain.Country;
import org.sopac.gem.domain.Organisation;
import org.sopac.gem.domain.Team;
import org.sopac.gem.domain.Team_;
import org.sopac.gem.repository.ContactRepository;
import org.sopac.gem.repository.CountryRepository;
import org.sopac.gem.repository.OrganisationRepository;
import org.sopac.gem.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CustomResource controller
 */
@RestController
@RequestMapping("/api/custom")
public class CustomResource {

    private final Logger log = LoggerFactory.getLogger(CustomResource.class);

    private final CountryRepository countryRepository;
    private final TeamRepository teamRepository;
    private final OrganisationRepository organisationRepository;
    private final ContactRepository contactRepository;

    public CustomResource(
        CountryRepository countryRepository,
        TeamRepository teamRepository,
        ContactRepository contactRepository,
        OrganisationRepository organisationRepository
    ) {
        this.countryRepository = countryRepository;
        this.teamRepository = teamRepository;
        this.contactRepository = contactRepository;
        this.organisationRepository = organisationRepository;
    }

    /**
     * GET populate
     */
    @GetMapping("/populate")
    public String populate() {
        countryRepository.deleteAll();
        teamRepository.deleteAll();
        organisationRepository.deleteAll();

        try {
            String path = "/mnt/c/Users/sachin/Documents/gep-data.csv";

            // countries
            Stream<String> lines = Files.lines(Paths.get(path));
            lines.forEach(
                l -> {
                    if (!l.split(",")[0].trim().equals("")) {
                        String name = l.split(",")[0];
                        Country c = new Country();
                        c.setName(name);
                        c.setMember(true);
                        countryRepository.save(c);
                    }
                }
            );
            lines.close();

            // teams
            lines = Files.lines(Paths.get(path));
            lines.forEach(
                l -> {
                    if (!l.split(",")[1].trim().equals("")) {
                        String name = l.split(",")[1];
                        String lead = l.split(",")[2];
                        Team t = new Team();
                        t.setName(name);
                        t.setLead(lead);
                        teamRepository.save(t);
                    }
                }
            );
            lines.close();

            // organisation
            lines = Files.lines(Paths.get(path));
            lines.forEach(
                l -> {
                    if (l.split(",").length > 3) {
                        String acronym = l.split(",")[3];
                        String name = l.split(",")[4];
                        Organisation o = new Organisation();
                        o.setName(name);
                        o.setAcronym(acronym);
                        organisationRepository.save(o);
                    }
                }
            );
            lines.close();
            // contacts

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }

        return "Population Finished.";
    }

    /**
     * GET sachin
     */
    @GetMapping("/sachin")
    public String sachin() {
        return "sachin";
    }
}
