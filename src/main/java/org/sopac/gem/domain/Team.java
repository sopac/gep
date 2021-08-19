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
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lead")
    private String lead;

    @OneToMany(mappedBy = "team")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resources", "countries", "team", "donor", "focalContact" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(mappedBy = "team")
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

    public Team id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLead() {
        return this.lead;
    }

    public Team lead(String lead) {
        this.lead = lead;
        return this;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Team projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Team addProject(Project project) {
        this.projects.add(project);
        project.setTeam(this);
        return this;
    }

    public Team removeProject(Project project) {
        this.projects.remove(project);
        project.setTeam(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setTeam(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setTeam(this));
        }
        this.projects = projects;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public Team proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public Team addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setTeam(this);
        return this;
    }

    public Team removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setTeam(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.setTeam(null));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.setTeam(this));
        }
        this.proposals = proposals;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lead='" + getLead() + "'" +
            "}";
    }
}
