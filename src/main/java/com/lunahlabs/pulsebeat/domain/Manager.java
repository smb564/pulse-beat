package com.lunahlabs.pulsebeat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Manager.
 */

@Document(collection = "manager")
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("company_id")
    private String companyId;

    @Field("department")
    private String department;

    @Field("user_id")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public Manager companyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDepartment() {
        return department;
    }

    public Manager department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserId() {
        return userId;
    }

    public Manager userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manager manager = (Manager) o;
        if (manager.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, manager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Manager{" +
            "id=" + id +
            ", companyId='" + companyId + "'" +
            ", department='" + department + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
