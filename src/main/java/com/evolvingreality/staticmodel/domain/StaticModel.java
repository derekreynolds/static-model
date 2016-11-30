package com.evolvingreality.staticmodel.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * A StaticModel.
 */
@Entity
@Table(name = "static_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaticModel extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ordinal")
    private Integer ordinal;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "static_key", length = 100, nullable = false)
    private String staticKey;

    @NotNull
    @Size(min = 2, max = 6)
    @Column(name = "locale", length = 6, nullable = false)
    private String locale;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "model_text", length = 255, nullable = false)
    private String modelText;
    
    @ManyToOne
    private StaticModelGroup staticModelGroup;
    

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getStaticKey() {
        return staticKey;
    }

    public void setStaticKey(String staticKey) {
        this.staticKey = staticKey;
    }
    
    public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getModelText() {
		return modelText;
	}

	public void setModelText(String modelText) {
		this.modelText = modelText;
	}


    public StaticModelGroup getStaticModelGroup() {
        return staticModelGroup;
    }

    public void setStaticModelGroup(StaticModelGroup staticModelGroup) {
        this.staticModelGroup = staticModelGroup;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StaticModel staticModel = (StaticModel) o;
        if(staticModel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, staticModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StaticModel{" +
            "id=" + id +
            ", ordinal='" + ordinal + "'" +
            ", staticKey='" + staticKey + "'" +
            ", locale='" + locale + "'" +
            ", modelText='" + modelText + "'" +
            '}';
    }
}
