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
import org.sopac.gem.domain.enumeration.Category;
import org.sopac.gem.domain.enumeration.Field;
import org.sopac.gem.domain.enumeration.Sector;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "staff")
    private Boolean staff;

    @Column(name = "designation")
    private String designation;

    @Column(name = "division")
    private String division;

    @Enumerated(EnumType.STRING)
    @Column(name = "field")
    private Field field;

    @Enumerated(EnumType.STRING)
    @Column(name = "sector")
    private Sector sector;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "city")
    private String city;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "skype")
    private String skype;

    @Column(name = "membership_affiliation")
    private String membershipAffiliation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "focalContact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor", "focalContact" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "contacts", "country" }, allowSetters = true)
    private Organisation organisation;

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

    public Contact id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Contact active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return this.category;
    }

    public Contact category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Contact photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Contact photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getName() {
        return this.name;
    }

    public Contact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStaff() {
        return this.staff;
    }

    public Contact staff(Boolean staff) {
        this.staff = staff;
        return this;
    }

    public void setStaff(Boolean staff) {
        this.staff = staff;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Contact designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDivision() {
        return this.division;
    }

    public Contact division(String division) {
        this.division = division;
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Field getField() {
        return this.field;
    }

    public Contact field(Field field) {
        this.field = field;
        return this;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Sector getSector() {
        return this.sector;
    }

    public Contact sector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getEmail() {
        return this.email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Contact phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return this.city;
    }

    public Contact city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public Contact linkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public Contact twitter(String twitter) {
        this.twitter = twitter;
        return this;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Contact facebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getSkype() {
        return this.skype;
    }

    public Contact skype(String skype) {
        this.skype = skype;
        return this;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getMembershipAffiliation() {
        return this.membershipAffiliation;
    }

    public Contact membershipAffiliation(String membershipAffiliation) {
        this.membershipAffiliation = membershipAffiliation;
        return this;
    }

    public void setMembershipAffiliation(String membershipAffiliation) {
        this.membershipAffiliation = membershipAffiliation;
    }

    public String getNotes() {
        return this.notes;
    }

    public Contact notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Contact projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Contact addProject(Project project) {
        this.projects.add(project);
        project.setFocalContact(this);
        return this;
    }

    public Contact removeProject(Project project) {
        this.projects.remove(project);
        project.setFocalContact(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setFocalContact(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setFocalContact(this));
        }
        this.projects = projects;
    }

    public Organisation getOrganisation() {
        return this.organisation;
    }

    public Contact organisation(Organisation organisation) {
        this.setOrganisation(organisation);
        return this;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Country getCountry() {
        return this.country;
    }

    public Contact country(Country country) {
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
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", category='" + getCategory() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", name='" + getName() + "'" +
            ", staff='" + getStaff() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", division='" + getDivision() + "'" +
            ", field='" + getField() + "'" +
            ", sector='" + getSector() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", city='" + getCity() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", skype='" + getSkype() + "'" +
            ", membershipAffiliation='" + getMembershipAffiliation() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
