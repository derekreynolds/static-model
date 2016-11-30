package com.evolvingreality.staticmodel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StaticModelGroup.
 */
@Entity
@Table(name = "static_model_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaticModelGroup extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "application_name")
    private String applicationName;
    
    @Column(name = "group_name")
    private String groupName;

    @OrderBy("ordinal ASC")
    @OneToMany(mappedBy = "staticModelGroup")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StaticModel> staticModels = new HashSet<>();

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Set<StaticModel> getStaticModels() {
        return staticModels;
    }

    public void setStaticModels(Set<StaticModel> staticModels) {
        this.staticModels = staticModels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StaticModelGroup staticModelGroup = (StaticModelGroup) o;
        if(staticModelGroup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, staticModelGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StaticModelGroup{" +
            "id=" + id +
            ", applicationName='" + applicationName + "'" +
            ", groupName='" + groupName + "'" +
            '}';
    }
}
