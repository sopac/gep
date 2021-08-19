package org.sopac.gem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Donor.
 */
@Entity
@Table(name = "donor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Donor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "city")
    private String city;

    @Column(name = "donor_category")
    private String donorCategory;

    @Column(name = "sector")
    private String sector;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "key_contact")
    private String keyContact;

    @OneToMany(mappedBy = "donor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor", "focalContact" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "donor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor" }, allowSetters = true)
    private Set<Proposal> proposals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "organisations", "donors", "contacts", "projects", "proposals" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donor id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Donor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Donor description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public Donor url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCity() {
        return this.city;
    }

    public Donor city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDonorCategory() {
        return this.donorCategory;
    }

    public Donor donorCategory(String donorCategory) {
        this.donorCategory = donorCategory;
        return this;
    }

    public void setDonorCategory(String donorCategory) {
        this.donorCategory = donorCategory;
    }

    public String getSector() {
        return this.sector;
    }

    public Donor sector(String sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getKeyContact() {
        return this.keyContact;
    }

    public Donor keyContact(String keyContact) {
        this.keyContact = keyContact;
        return this;
    }

    public void setKeyContact(String keyContact) {
        this.keyContact = keyContact;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Donor projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Donor addProject(Project project) {
        this.projects.add(project);
        project.setDonor(this);
        return this;
    }

    public Donor removeProject(Project project) {
        this.projects.remove(project);
        project.setDonor(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setDonor(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setDonor(this));
        }
        this.projects = projects;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public Donor proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public Donor addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setDonor(this);
        return this;
    }

    public Donor removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setDonor(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.setDonor(null));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.setDonor(this));
        }
        this.proposals = proposals;
    }

    public Country getCountry() {
        return this.country;
    }

    public Donor country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Donor)) {
            return false;
        }
        return id != null && id.equals(((Donor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Donor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", city='" + getCity() + "'" +
            ", donorCategory='" + getDonorCategory() + "'" +
            ", sector='" + getSector() + "'" +
            ", keyContact='" + getKeyContact() + "'" +
            "}";
    }
}
