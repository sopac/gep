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
import org.sopac.gem.domain.enumeration.Currency;
import org.sopac.gem.domain.enumeration.Status;

/**
 * A Proposal.
 */
@Entity
@Table(name = "proposal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "divsion_endorsement")
    private Boolean divsionEndorsement;

    @Column(name = "pacific_community_endorsement")
    private Boolean pacificCommunityEndorsement;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_budget_currency")
    private Currency totalBudgetCurrency;

    @Column(name = "total_budget")
    private Double totalBudget;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "total_budget_breakdown")
    private String totalBudgetBreakdown;

    @Column(name = "key_thematic_areas")
    private String keyThematicAreas;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "lessons_learnt_best_practices")
    private String lessonsLearntBestPractices;

    @Column(name = "full_cost_recovery_coverage")
    private String fullCostRecoveryCoverage;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    private String notes;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_proposal__resource",
        joinColumns = @JoinColumn(name = "proposal_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    @JsonIgnoreProperties(value = { "projects", "proposals" }, allowSetters = true)
    private Set<Resource> resources = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_proposal__country",
        joinColumns = @JoinColumn(name = "proposal_id"),
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proposal id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Proposal title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Proposal description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return this.status;
    }

    public Proposal status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getDivsionEndorsement() {
        return this.divsionEndorsement;
    }

    public Proposal divsionEndorsement(Boolean divsionEndorsement) {
        this.divsionEndorsement = divsionEndorsement;
        return this;
    }

    public void setDivsionEndorsement(Boolean divsionEndorsement) {
        this.divsionEndorsement = divsionEndorsement;
    }

    public Boolean getPacificCommunityEndorsement() {
        return this.pacificCommunityEndorsement;
    }

    public Proposal pacificCommunityEndorsement(Boolean pacificCommunityEndorsement) {
        this.pacificCommunityEndorsement = pacificCommunityEndorsement;
        return this;
    }

    public void setPacificCommunityEndorsement(Boolean pacificCommunityEndorsement) {
        this.pacificCommunityEndorsement = pacificCommunityEndorsement;
    }

    public Currency getTotalBudgetCurrency() {
        return this.totalBudgetCurrency;
    }

    public Proposal totalBudgetCurrency(Currency totalBudgetCurrency) {
        this.totalBudgetCurrency = totalBudgetCurrency;
        return this;
    }

    public void setTotalBudgetCurrency(Currency totalBudgetCurrency) {
        this.totalBudgetCurrency = totalBudgetCurrency;
    }

    public Double getTotalBudget() {
        return this.totalBudget;
    }

    public Proposal totalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
        return this;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getTotalBudgetBreakdown() {
        return this.totalBudgetBreakdown;
    }

    public Proposal totalBudgetBreakdown(String totalBudgetBreakdown) {
        this.totalBudgetBreakdown = totalBudgetBreakdown;
        return this;
    }

    public void setTotalBudgetBreakdown(String totalBudgetBreakdown) {
        this.totalBudgetBreakdown = totalBudgetBreakdown;
    }

    public String getKeyThematicAreas() {
        return this.keyThematicAreas;
    }

    public Proposal keyThematicAreas(String keyThematicAreas) {
        this.keyThematicAreas = keyThematicAreas;
        return this;
    }

    public void setKeyThematicAreas(String keyThematicAreas) {
        this.keyThematicAreas = keyThematicAreas;
    }

    public String getLessonsLearntBestPractices() {
        return this.lessonsLearntBestPractices;
    }

    public Proposal lessonsLearntBestPractices(String lessonsLearntBestPractices) {
        this.lessonsLearntBestPractices = lessonsLearntBestPractices;
        return this;
    }

    public void setLessonsLearntBestPractices(String lessonsLearntBestPractices) {
        this.lessonsLearntBestPractices = lessonsLearntBestPractices;
    }

    public String getFullCostRecoveryCoverage() {
        return this.fullCostRecoveryCoverage;
    }

    public Proposal fullCostRecoveryCoverage(String fullCostRecoveryCoverage) {
        this.fullCostRecoveryCoverage = fullCostRecoveryCoverage;
        return this;
    }

    public void setFullCostRecoveryCoverage(String fullCostRecoveryCoverage) {
        this.fullCostRecoveryCoverage = fullCostRecoveryCoverage;
    }

    public String getNotes() {
        return this.notes;
    }

    public Proposal notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public Proposal resources(Set<Resource> resources) {
        this.setResources(resources);
        return this;
    }

    public Proposal addResource(Resource resource) {
        this.resources.add(resource);
        resource.getProposals().add(this);
        return this;
    }

    public Proposal removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.getProposals().remove(this);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public Proposal countries(Set<Country> countries) {
        this.setCountries(countries);
        return this;
    }

    public Proposal addCountry(Country country) {
        this.countries.add(country);
        country.getProposals().add(this);
        return this;
    }

    public Proposal removeCountry(Country country) {
        this.countries.remove(country);
        country.getProposals().remove(this);
        return this;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public Team getTeam() {
        return this.team;
    }

    public Proposal team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Donor getDonor() {
        return this.donor;
    }

    public Proposal donor(Donor donor) {
        this.setDonor(donor);
        return this;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposal)) {
            return false;
        }
        return id != null && id.equals(((Proposal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proposal{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", divsionEndorsement='" + getDivsionEndorsement() + "'" +
            ", pacificCommunityEndorsement='" + getPacificCommunityEndorsement() + "'" +
            ", totalBudgetCurrency='" + getTotalBudgetCurrency() + "'" +
            ", totalBudget=" + getTotalBudget() +
            ", totalBudgetBreakdown='" + getTotalBudgetBreakdown() + "'" +
            ", keyThematicAreas='" + getKeyThematicAreas() + "'" +
            ", lessonsLearntBestPractices='" + getLessonsLearntBestPractices() + "'" +
            ", fullCostRecoveryCoverage='" + getFullCostRecoveryCoverage() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
