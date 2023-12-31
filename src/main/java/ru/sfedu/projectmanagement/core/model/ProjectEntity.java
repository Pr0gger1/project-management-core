package ru.sfedu.projectmanagement.core.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvIgnore;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ru.sfedu.projectmanagement.core.Constants;
import ru.sfedu.projectmanagement.core.model.enums.EntityType;
import ru.sfedu.projectmanagement.core.utils.xml.adapters.XmlLocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectEntity")
public abstract class ProjectEntity implements Entity {
    @CsvIgnore
    @XmlTransient
    private EntityType entityType;

    @CsvBindByName(column = "id", required = true)
    @XmlAttribute(required = true)
    protected UUID id;

    @CsvBindByName(column = "name", required = true)
    @XmlElement(required = true)
    protected String name;

    @CsvBindByName(column = "description")
    @XmlElement(nillable = true)
    protected String description;

    @CsvBindByName(column = "project_id", required = true)
    @XmlElement(required = true)
    protected UUID projectId;

    @CsvBindByName(column = "employee_id", required = true)
    @XmlElement(required = true)
    protected UUID employeeId;

    @CsvBindByName(column = "employee_full_name", required = true)
    @XmlElement(required = true)
    protected String employeeFullName;

    @CsvBindByName(column = "createdAt", required = true)
    @CsvDate(value = Constants.LOCAL_DATETIME_FORMAT)
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(XmlLocalDateTimeAdapter.class)
    protected LocalDateTime createdAt;

    ProjectEntity() {}

    ProjectEntity(EntityType entityType) {
        id = UUID.randomUUID();
        createdAt = LocalDateTime.now().withNano(0);
        this.entityType = entityType;
    }

    ProjectEntity(
            String name,
            String description,
            UUID employeeId,
            String employeeFullName,
            UUID projectId,
            EntityType entityType
    ) {
        this.entityType = entityType;
        createdAt = LocalDateTime.now().withNano(0);
        this.id = UUID.randomUUID();
        this.name = name;
        this.projectId = projectId;
        this.description = description;
        this.employeeId = employeeId;
        this.employeeFullName = employeeFullName;
    }


    // full constructor
    ProjectEntity(
            String name,
            String description,
            UUID id,
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            LocalDateTime createdAt,
            EntityType entityType
    ) {
        this.entityType = entityType;
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.employeeFullName = employeeFullName;
        this.createdAt = createdAt;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProjectId(UUID id) {
        this.projectId = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProjectEntity that = (ProjectEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(projectId, that.projectId) && Objects.equals(employeeId, that.employeeId) && Objects.equals(employeeFullName, that.employeeFullName) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, projectId, employeeId, employeeFullName, createdAt);
    }
}
