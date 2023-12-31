package ru.sfedu.projectmanagement.core.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import ru.sfedu.projectmanagement.core.model.enums.EntityType;
import ru.sfedu.projectmanagement.core.utils.xml.adapters.XmlLocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Event extends ProjectEntity {
    @CsvBindByName(column = "start_date")
    @CsvDate
    @XmlElement(name = "start_date")
    @XmlJavaTypeAdapter(XmlLocalDateTimeAdapter.class)
    private LocalDateTime startDate;

    @CsvBindByName(column = "end_date")
    @CsvDate
    @XmlElement(name = "end_date")
    @XmlJavaTypeAdapter(XmlLocalDateTimeAdapter.class)
    private LocalDateTime endDate;

    public Event() {
        super(EntityType.Event);
    }

    public Event(
            String name,
            String description,
            UUID employeeId,
            String employeeFullName,
            UUID projectId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        super(name, description, employeeId, employeeFullName, projectId, EntityType.Event);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(
            String name,
            String description,
            UUID id,
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            LocalDateTime createdAt,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        super(name, description, id, projectId, employeeId, employeeFullName, createdAt, EntityType.Event);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Event{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                ", employeeId=" + employeeId +
                ", employeeFullName='" + employeeFullName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Event event = (Event) object;
        return Objects.equals(startDate, event.startDate) && Objects.equals(endDate, event.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
