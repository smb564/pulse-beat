package com.lunahlabs.pulsebeat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CompanyAdmin.
 */

@Document(collection = "company_admin")
public class CompanyAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("company_id")
    private String companyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public CompanyAdmin companyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyAdmin companyAdmin = (CompanyAdmin) o;
        if (companyAdmin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyAdmin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyAdmin{" +
            "id=" + id +
            ", companyId='" + companyId + "'" +
            '}';
    }
}
