package com.jungles.rsjapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Fera.
 */
@Entity
@Table(name = "fera")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fera_name")
    private String feraName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fera id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeraName() {
        return this.feraName;
    }

    public Fera feraName(String feraName) {
        this.setFeraName(feraName);
        return this;
    }

    public void setFeraName(String feraName) {
        this.feraName = feraName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fera)) {
            return false;
        }
        return id != null && id.equals(((Fera) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fera{" +
            "id=" + getId() +
            ", feraName='" + getFeraName() + "'" +
            "}";
    }
}
