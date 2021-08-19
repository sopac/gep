package org.sopac.gem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.sopac.gem.domain.enumeration.OrganisationCategory;

/**
 * A Organisation.
 */
@Entity
@Table(name = "organisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "acronym")
    private String acronym;

    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_category")
    private OrganisationCategory organisationCategory;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "organisation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projects", "organisation", "country" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

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

    public Organisation id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Organisation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public Organisation acronym(String acronym) {
        this.acronym = acronym;
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public OrganisationCategory getOrganisationCategory() {
        return this.organisationCategory;
    }

    public Organisation organisationCategory(OrganisationCategory organisationCategory) {
        this.organisationCategory = organisationCategory;
        return this;
    }

    public void setOrganisationCategory(OrganisationCategory organisationCategory) {
        this.organisationCategory = organisationCategory;
    }

    public String getUrl() {
        return this.url;
    }

    public Organisation url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public Organisation contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Organisation addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setOrganisation(this);
        return this;
    }

    public Organisation removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setOrganisation(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setOrganisation(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setOrganisation(this));
        }
        this.contacts = contacts;
    }

    public Country getCountry() {
        return this.country;
    }

    public Organisation country(Country country) {
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
        if (!(o instanceof Organisation)) {
            return false;
        }
        return id != null && id.equals(((Organisation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", acronym='" + getAcronym() + "'" +
            ", organisationCategory='" + getOrganisationCategory() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
