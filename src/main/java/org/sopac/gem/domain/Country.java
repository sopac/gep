package org.sopac.gem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "member")
    private Boolean member;

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "country" }, allowSetters = true)
    private Set<Organisation> organisations = new HashSet<>();

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projects", "proposals", "country" }, allowSetters = true)
    private Set<Donor> donors = new HashSet<>();

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projects", "organisation", "country" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor", "focalContact" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "countries")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor" }, allowSetters = true)
    private Set<Proposal> proposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMember() {
        return this.member;
    }

    public Country member(Boolean member) {
        this.member = member;
        return this;
    }

    public void setMember(Boolean member) {
        this.member = member;
    }

    public Set<Organisation> getOrganisations() {
        return this.organisations;
    }

    public Country organisations(Set<Organisation> organisations) {
        this.setOrganisations(organisations);
        return this;
    }

    public Country addOrganisation(Organisation organisation) {
        this.organisations.add(organisation);
        organisation.setCountry(this);
        return this;
    }

    public Country removeOrganisation(Organisation organisation) {
        this.organisations.remove(organisation);
        organisation.setCountry(null);
        return this;
    }

    public void setOrganisations(Set<Organisation> organisations) {
        if (this.organisations != null) {
            this.organisations.forEach(i -> i.setCountry(null));
        }
        if (organisations != null) {
            organisations.forEach(i -> i.setCountry(this));
        }
        this.organisations = organisations;
    }

    public Set<Donor> getDonors() {
        return this.donors;
    }

    public Country donors(Set<Donor> donors) {
        this.setDonors(donors);
        return this;
    }

    public Country addDonor(Donor donor) {
        this.donors.add(donor);
        donor.setCountry(this);
        return this;
    }

    public Country removeDonor(Donor donor) {
        this.donors.remove(donor);
        donor.setCountry(null);
        return this;
    }

    public void setDonors(Set<Donor> donors) {
        if (this.donors != null) {
            this.donors.forEach(i -> i.setCountry(null));
        }
        if (donors != null) {
            donors.forEach(i -> i.setCountry(this));
        }
        this.donors = donors;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public Country contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Country addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setCountry(this);
        return this;
    }

    public Country removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setCountry(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setCountry(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setCountry(this));
        }
        this.contacts = contacts;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Country projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Country addProject(Project project) {
        this.projects.add(project);
        project.getCountries().add(this);
        return this;
    }

    public Country removeProject(Project project) {
        this.projects.remove(project);
        project.getCountries().remove(this);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.removeCountry(this));
        }
        if (projects != null) {
            projects.forEach(i -> i.addCountry(this));
        }
        this.projects = projects;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public Country proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public Country addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.getCountries().add(this);
        return this;
    }

    public Country removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.getCountries().remove(this);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.removeCountry(this));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.addCountry(this));
        }
        this.proposals = proposals;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", member='" + getMember() + "'" +
            "}";
    }
}
