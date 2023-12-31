package ru.sfedu.projectmanagement.core.api;

import org.junit.jupiter.api.BeforeAll;
import ru.sfedu.projectmanagement.core.model.*;
import ru.sfedu.projectmanagement.core.model.enums.BugStatus;
import ru.sfedu.projectmanagement.core.model.enums.Priority;
import ru.sfedu.projectmanagement.core.model.enums.WorkStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class BaseProviderTest {
    protected static final ArrayList<Employee> team = new ArrayList<>();
    private static final UUID staticEmployeeId1 = UUID.fromString("0c725486-f5ed-4d12-819e-4e5bd7eb1a9d");
    private static final UUID staticEmployeeId2 = UUID.fromString("ebaf9fd8-b97a-430e-ab91-fb3493410a93");

    protected static final Employee employee1 = createEmployee(
            staticEmployeeId1,
            "Nikolay",
            "Eremeev",
            "Ivanovich",
            LocalDate.of(1999, Month.MAY, 6),
            "+79882458565",
            "Senior mobile dev lead",
            "mail@mail.ru"
    );

    protected static final Employee employee2 = createEmployee(
            staticEmployeeId2,
            "Petr",
            "Zaycev",
            "Alexeevich",
            LocalDate.of(1999, Month.MAY, 6),
            "+79882258565",
            "Senior mobile dev lead",
            "zaycev@mail.ru"
    );


    protected static final Project project1 = createProject(
            UUID.randomUUID(),
            "mobile bank app",
            "mobile app for bank based on kotlin and swift",
            WorkStatus.IN_PROGRESS,
            LocalDateTime.of(2025, Month.MARCH, 1, 0,0),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(List.of(employee1)),
            employee1
    );

    protected static final Project project2 = createProject(
            UUID.randomUUID(),
            "mobile bank app",
            "mobile app for bank based on kotlin and swift",
            WorkStatus.IN_PROGRESS,
            LocalDateTime.of(2025, Month.MARCH, 1, 0,0),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            null
    );

    protected static Task task = createTask(
            project1.getId(),
            employee1.getId(),
            employee1.getFullName(),
            "task",
            "task description",
            "create main page of application",
            WorkStatus.IN_PROGRESS,
            new ArrayList<>(),
            null,
            LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0),
            Priority.MEDIUM
    );


    protected static final BugReport bugReport = new BugReport(
            "mobile_bank_report_12-05-2023",
            "this is a bug report description",
            employee1.getId(),
            employee1.getFullName(),
            project1.getId(),
            Priority.HIGH
    );

    protected static final Event event = new Event(
            "mobile bank app presentation",
            "show client what we did",
            employee1.getId(),
            employee1.getFullName(),
            project1.getId(),
            LocalDateTime.of(2023, Month.DECEMBER, 22, 15, 0),
            LocalDateTime.of(2023, Month.DECEMBER, 22, 10, 0)
    );

    protected static final Documentation documentation = new Documentation(
            "app documentation",
            "app documentation description",
            new HashMap<>() {{
                put("chapter one", "some loooooong text");
                put("chapter two", "another some loooooong text");
            }},
            employee1.getId(),
            employee1.getFullName(),
            project1.getId()
    );

    protected static final ArrayList<Task> tasks = new ArrayList<>();
    protected static final ArrayList<BugReport> bugReports = new ArrayList<>();
    protected final static ArrayList<Event> events = new ArrayList<>(){{add(event);}};


    @BeforeAll
    public static void createTaskList() {
        Task task1 = createTask(
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                "Task 1",
                "Description for Task 1",
                "Comment for Task 1",
                WorkStatus.COMPLETED,
                new ArrayList<>(Arrays.asList("Tag1", "tag2")),
                LocalDateTime.of(2023, Month.DECEMBER, 20, 15, 12),
                LocalDateTime.of(2023, Month.DECEMBER, 24,0, 0),
                Priority.HIGH
        );

        Task task2 = createTask(
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                "Task 2",
                "Description for Task 2",
                "Comment for Task 2",
                WorkStatus.COMPLETED,
                new ArrayList<>(Arrays.asList("Tag1", "tag2")),
                LocalDateTime.of(2023, Month.NOVEMBER, 18, 10, 54),
                LocalDateTime.of(2023, Month.NOVEMBER, 15,0, 0),
                Priority.MEDIUM
        );

        Task task3 = createTask(
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                "Task 3",
                "Description for Task 3",
                "Comment for Task 3",
                WorkStatus.IN_PROGRESS,
                new ArrayList<>(Arrays.asList("Tag1", "tag2")),
                null,
                LocalDateTime.of(2023, Month.DECEMBER, 15, 0, 0),
                Priority.LOW
        );

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
    }

    @BeforeAll
    static void createTeamList() {
        LocalDate birthday1 = LocalDate.of(1990, Month.MAY, 15);
        Employee employee1 = createEmployee(
                UUID.randomUUID(),
                "Иван",
                "Иванов",
                "Иванович",
                birthday1,
                "+79004526387",
                "mail@example.com",
                "Разработчик"
        );
        team.add(employee1);

        LocalDate birthday2 = LocalDate.of(1985, Month.MARCH, 14);
        Employee employee2 = createEmployee(
                UUID.randomUUID(),
                "Елена",
                "Петрова",
                "Александровна",
                birthday2,
                "+79004552387",
                "mail@example.com",
                "Тестировщик"
        );
        team.add(employee2);

        LocalDate birthday3 = LocalDate.of(1992, Month.AUGUST, 7);
        Employee employee3 = createEmployee(
                UUID.randomUUID(),
                "Андрей",
                "Смирнов",
                null,
                birthday3,
                "+799804526367",
                "mail@example.com",
                "Дизайнер"
        );
        team.add(employee3);

        project2.setTeam(team);
    }

    @BeforeAll
    static void createBugReportList() {
        BugReport bugReport1 = createBugReport(
                "Bug 1",
                "Description 1",
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                BugStatus.IN_PROGRESS,
                Priority.LOW
        );

        BugReport bugReport2 = createBugReport(
                "Bug 2",
                "Description 2",
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                BugStatus.OPENED,
                Priority.HIGH
        );

        BugReport bugReport3 = createBugReport(
                "Bug 3",
                "Description 3",
                project1.getId(),
                employee1.getId(),
                employee1.getFullName(),
                BugStatus.CLOSED,
                Priority.MEDIUM
        );
        bugReport3.setStatus(BugStatus.CLOSED);

        bugReports.add(bugReport1);
        bugReports.add(bugReport2);
        bugReports.add(bugReport3);
    }


    static protected Project createProject(
            UUID id,
            String name,
            String description,
            WorkStatus status,
            LocalDateTime deadline,
            ArrayList<Task> tasks,
            ArrayList<BugReport> bugReports,
            ArrayList<Event> events,
            ArrayList<Documentation> documentations,
            ArrayList<Employee> team,
            Employee manager
    ) {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        project.setTasks(tasks);
        project.setEvents(events);
        project.setBugReports(bugReports);
        project.setDocumentations(documentations);
        project.setTeam(team);
        project.setStatus(status);
        project.setDeadline(deadline);
        if (manager != null)
            project.setManager(manager);
        
        return project;
    }
    
    static protected Task createTask(
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            String name,
            String description,
            String comment,
            WorkStatus status,
            ArrayList<String> tags,
            LocalDateTime completedAt,
            LocalDateTime deadline,
            Priority priority
    ) {
        Task task = new Task();
        task.setProjectId(projectId);
        task.setEmployeeFullName(employeeFullName);
        task.setEmployeeId(employeeId);
        task.setName(name);
        task.setDescription(description);
        task.setComment(comment);
        task.setStatus(status);
        task.setCompletedAt(completedAt);
        task.setTags(tags);
        task.setDeadline(deadline);
        task.setPriority(priority);
        
        return task;
    }

    static protected BugReport createBugReport(
            String name,
            String description,
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            BugStatus status,
            Priority priority
    ) {
        BugReport bugReport = new BugReport();
        bugReport.setProjectId(projectId);
        bugReport.setPriority(priority);
        bugReport.setEmployeeId(employeeId);
        bugReport.setEmployeeFullName(employeeFullName);
        bugReport.setStatus(status);
        bugReport.setName(name);
        bugReport.setDescription(description);

        return bugReport;
    }

    static protected Event createEvent(
            String name,
            String description,
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setProjectId(projectId);
        event.setEmployeeId(employeeId);
        event.setEmployeeFullName(employeeFullName);
        event.setEndDate(endDate);
        event.setStartDate(startDate);

        return event;
    }

    static protected Documentation createDocumentation(
            String name,
            String description,
            UUID projectId,
            UUID employeeId,
            String employeeFullName,
            HashMap<String, String> body
    ) {
        Documentation documentation = new Documentation();
        documentation.setName(name);
        documentation.setProjectId(projectId);
        documentation.setDescription(description);
        documentation.setEmployeeId(employeeId);
        documentation.setEmployeeFullName(employeeFullName);
        documentation.setBody(body);
        return documentation;
    }

    static protected Employee createEmployee(
            UUID id,
            String firstName,
            String lastName,
            String patronymic,
            LocalDate birthday,
            String phoneNumber,
            String position,
            String email
    ) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPatronymic(patronymic);
        employee.setBirthday(birthday);
        employee.setPhoneNumber(phoneNumber);
        employee.setPosition(position);
        employee.setEmail(email);

        return employee;
    }

    public void initDataForMonitorProjectCharacteristics(IDataProvider provider) {
        tasks.forEach(provider::processNewTask);
        provider.bindEmployeeToProject(employee1.getId(), project1.getId());

    }
}
