package org.sopac.gem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.sopac.gem.domain.enumeration.Currency;
import org.sopac.gem.domain.enumeration.Status;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_budget_currency")
    private Currency totalBudgetCurrency;

    @Column(name = "total_budget")
    private Double totalBudget;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "total_budget_breakdown")
    private String totalBudgetBreakdown;

    @Column(name = "sustainable_development_goal")
    private String sustainableDevelopmentGoal;

    @Column(name = "framework_resilient_development_pacific")
    private String frameworkResilientDevelopmentPacific;

    @Column(name = "full_cost_recovery_coverage")
    private String fullCostRecoveryCoverage;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_project__resource",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    @JsonIgnoreProperties(value = { "projects", "proposals" }, allowSetters = true)
    private Set<Resource> resources = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_project__country",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    @JsonIgnoreProperties(value = { "organisations", "donors", "contacts", "projects", "proposals" }, allowSetters = true)
    private Set<Country> countries = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "projects", "proposals" }, allowSetters = true)
    private Team team;

    @ManyToOne
    @JsonIgnoreProperties(value = { "projects", "proposals", "country" }, allowSetters = true)
    private Donor donor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "projects", "organisation", "country" }, allowSetters = true)
    private Contact focalContact;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Project title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Project startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Project endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Project status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Currency getTotalBudgetCurrency() {
        return this.totalBudgetCurrency;
    }

    public Project totalBudgetCurrency(Currency totalBudgetCurrency) {
        this.totalBudgetCurrency = totalBudgetCurrency;
        return this;
    }

    public void setTotalBudgetCurrency(Currency totalBudgetCurrency) {
        this.totalBudgetCurrency = totalBudgetCurrency;
    }

    public Double getTotalBudget() {
        return this.totalBudget;
    }

    public Project totalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
        return this;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getTotalBudgetBreakdown() {
        return this.totalBudgetBreakdown;
    }

    public Project totalBudgetBreakdown(String totalBudgetBreakdown) {
        this.totalBudgetBreakdown = totalBudgetBreakdown;
        return this;
    }

    public void setTotalBudgetBreakdown(String totalBudgetBreakdown) {
        this.totalBudgetBreakdown = totalBudgetBreakdown;
    }

    public String getSustainableDevelopmentGoal() {
        return this.sustainableDevelopmentGoal;
    }

    public Project sustainableDevelopmentGoal(String sustainableDevelopmentGoal) {
        this.sustainableDevelopmentGoal = sustainableDevelopmentGoal;
        return this;
    }

    public void setSustainableDevelopmentGoal(String sustainableDevelopmentGoal) {
        this.sustainableDevelopmentGoal = sustainableDevelopmentGoal;
    }

    public String getFrameworkResilientDevelopmentPacific() {
        return this.frameworkResilientDevelopmentPacific;
    }

    public Project frameworkResilientDevelopmentPacific(String frameworkResilientDevelopmentPacific) {
        this.frameworkResilientDevelopmentPacific = frameworkResilientDevelopmentPacific;
        return this;
    }

    public void setFrameworkResilientDevelopmentPacific(String frameworkResilientDevelopmentPacific) {
        this.frameworkResilientDevelopmentPacific = frameworkResilientDevelopmentPacific;
    }

    public String getFullCostRecoveryCoverage() {
        return this.fullCostRecoveryCoverage;
    }

    public Project fullCostRecoveryCoverage(String fullCostRecoveryCoverage) {
        this.fullCostRecoveryCoverage = fullCostRecoveryCoverage;
        return this;
    }

    public void setFullCostRecoveryCoverage(String fullCostRecoveryCoverage) {
        this.fullCostRecoveryCoverage = fullCostRecoveryCoverage;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public Project resources(Set<Resource> resources) {
        this.setResources(resources);
        return this;
    }

    public Project addResource(Resource resource) {
        this.resources.add(resource);
        resource.getProjects().add(this);
        return this;
    }

    public Project removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.getProjects().remove(this);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public Project countries(Set<Country> countries) {
        this.setCountries(countries);
        return this;
    }

    public Project addCountry(Country country) {
        this.countries.add(country);
        country.getProjects().add(this);
        return this;
    }

    public Project removeCountry(Country country) {
        this.countries.remove(country);
        country.getProjects().remove(this);
        return this;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public Team getTeam() {
        return this.team;
    }

    public Project team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Donor getDonor() {
        return this.donor;
    }

    public Project donor(Donor donor) {
        this.setDonor(donor);
        return this;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Contact getFocalContact() {
        return this.focalContact;
    }

    public Project focalContact(Contact contact) {
        this.setFocalContact(contact);
        return this;
    }

    public void setFocalContact(Contact contact) {
        this.focalContact = contact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalBudgetCurrency='" + getTotalBudgetCurrency() + "'" +
            ", totalBudget=" + getTotalBudget() +
            ", totalBudgetBreakdown='" + getTotalBudgetBreakdown() + "'" +
            ", sustainableDevelopmentGoal='" + getSustainableDevelopmentGoal() + "'" +
            ", frameworkResilientDevelopmentPacific='" + getFrameworkResilientDevelopmentPacific() + "'" +
            ", fullCostRecoveryCoverage='" + getFullCostRecoveryCoverage() + "'" +
            "}";
    }
}
