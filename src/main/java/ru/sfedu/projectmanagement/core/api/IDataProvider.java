package ru.sfedu.projectmanagement.core.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.projectmanagement.core.model.*;
import ru.sfedu.projectmanagement.core.model.enums.ChangeType;
import ru.sfedu.projectmanagement.core.model.enums.WorkStatus;
import ru.sfedu.projectmanagement.core.utils.types.HistoryRecord;
import ru.sfedu.projectmanagement.core.utils.types.NoData;
import ru.sfedu.projectmanagement.core.utils.types.Result;
import ru.sfedu.projectmanagement.core.utils.types.TrackInfo;
import ru.sfedu.projectmanagement.core.model.enums.ActionStatus;
import ru.sfedu.projectmanagement.core.utils.ResultCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public interface IDataProvider {
    Logger logger = LogManager.getLogger(IDataProvider.class);

    /**
     *
     * @param entity entity which will be saved in mongo db
     * @param methodName name of the method that runs logEntity. Necessary for logs
     * @param queryResult execution result of entity operation
     * @param changeType type of what happened with entity
     */
    default void logEntity(Object entity, String methodName, ResultCode queryResult, ChangeType changeType) {
        ActionStatus status = queryResult == ResultCode.SUCCESS ? ActionStatus.SUCCESS : ActionStatus.FAULT;
        HistoryRecord<Object> historyRecord =  new HistoryRecord<>(
                entity,
                methodName,
                status,
                changeType
        );

        logger.debug("logEntity[1]: object {} saved to history", entity);
        MongoHistoryProvider.save(historyRecord);
    }

    /** Creates new project
     * @param project Project instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewProject(Project project);

    /**
     * @param task Task instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewTask(Task task);

    /**
     * @param bugReport BugReport instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewBugReport(BugReport bugReport);

    /**
     * @param documentation Documentation instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewDocumentation(Documentation documentation);

    /**
     * @param event Event instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewEvent(Event event);

    /**
     * @param employee Employee instance
     * @return Result with execution code and message if it fails
     */
    Result<NoData> processNewEmployee(Employee employee);


    /**
     * @param projectId id of the project
     * @param checkLaborEfficiency boolean flag which includes information about employees labor efficiency if true
     * @param trackBugs boolean flag which includes information about bug reports status if true
     * @return ProjectStatistics object
     */
    default ProjectStatistics monitorProjectCharacteristics(
            UUID projectId, boolean checkLaborEfficiency, boolean trackBugs
    ) {
        ProjectStatistics statistics = new ProjectStatistics();

        statistics.setProjectReadiness(calculateProjectReadiness(projectId));
        statistics.setTaskStatus(trackTaskStatus(projectId));

        if (checkLaborEfficiency)
            statistics.setLaborEfficiency(calculateLaborEfficiency(projectId));
        if (trackBugs)
            statistics.setBugReportStatus(trackBugReportStatus(projectId));

        return statistics;
    }

    /**
     * @param projectId id of the project
     * @return the percentage of the project readiness. It is calculated by number of completed tasks
     */
    default float calculateProjectReadiness(UUID projectId) {
        List<Task> tasks = getTasksByProjectId(projectId).getData();
        if (!tasks.isEmpty()) {
            int countOfCompletedTasks = (int) tasks.stream()
                    .filter(task -> task.getStatus() == WorkStatus.COMPLETED).count();

            return ((float) countOfCompletedTasks / tasks.size()) * 100.0f;
        }
        return 0;
    }


    /**
     * @param projectId id of the project
     * @return TrackInfo with employee and his percentage of labor.
     * Efficiency is calculated based on tasks completed on time
     */
    default TrackInfo<Employee, Float> calculateLaborEfficiency(UUID projectId) {
        List<Employee> team = getProjectTeam(projectId).getData();
        TrackInfo<Employee, Float> result = new TrackInfo<>();

        if (team.isEmpty()) return new TrackInfo<>();

        team.forEach(employee -> {
            List<Task> employeeTasks = getTasksByEmployeeId(employee.getId()).getData();
            if (employeeTasks.isEmpty()) {
                result.addData(employee, 0f);
                return;
            }

            float averageEffectiveness = calculateEmployeeEfficiency(employeeTasks);
            result.addData(employee, averageEffectiveness);
        });

        return result;
    }

    /**
     * @param projectId id of the project
     * @return TrackInfo with task and its status
     */
    default TrackInfo<Task, String> trackTaskStatus(UUID projectId) {
        List<Task> tasks = getTasksByProjectId(projectId).getData();
        return Optional.of(tasks)
                .filter(t -> !t.isEmpty())
                .map(t -> {
                    TrackInfo<Task, String> result = new TrackInfo<>();
                    t.forEach(task -> result.addData(task, task.getStatus().name()));
                    return result;
                })
                .orElse(new TrackInfo<>());
    }


    /**
     * @param projectId id of the project
     * @return TrackInfo with bug report and its status
     */
    default TrackInfo<BugReport, String> trackBugReportStatus(UUID projectId) {
        List<BugReport> bugReports = getBugReportsByProjectId(projectId).getData();
        return Optional.of(bugReports)
                .filter(bg -> !bg.isEmpty())
                .map(bg -> {
                    TrackInfo<BugReport, String> result = new TrackInfo<>();
                    bg.forEach(el -> result.addData(el, el.getStatus().name()));
                    return result;
                })
                .orElse(new TrackInfo<>());
    }

    /**
     *
     * @param tasks list with tasks
     * @return the percentage of efficiency of a particular employee
     */
    default float calculateEmployeeEfficiency(List<Task> tasks) {
        int tasksCount = tasks.size();
        // percentage of execution tasks efficiency
        int taskEffectivenessSum = 0;

        for (Task task : tasks) {
            LocalDateTime taskDeadline = task.getDeadline();

            if (task.getStatus() == WorkStatus.COMPLETED && task.getCompletedAt() != null) {

                // difference in days between date of completion and deadline
                long timeDifference = Math.abs(Duration.between(taskDeadline, task.getCompletedAt()).toDays());
                if (taskDeadline.isBefore(task.getCompletedAt()))
                    taskEffectivenessSum += (int) (100 - timeDifference);
                else taskEffectivenessSum += (int) (100 + timeDifference);
            }
            else if (task.getStatus() == WorkStatus.IN_PROGRESS) {
                // if task is overdue - calculate work efficiency by subtracting the number of days from 100%
                // else task execution efficiency equals 0
                if (taskDeadline.isBefore(LocalDateTime.now())) {
                    // difference in days between current date and deadline
                    long timeDifference = Math.abs(Duration.between(taskDeadline, LocalDateTime.now()).toDays());
                    taskEffectivenessSum += (int) (100 - timeDifference);
                }
            }
        }
        return (taskEffectivenessSum * 1.0f) / tasksCount;
    }

    Result<NoData> bindEmployeeToProject(UUID employeeId, UUID projectId);

    /**
     * @param managerId id of the manager who binds to the project
     * @param projectId id of the project to which the manager is attached
     */
    Result<NoData> bindProjectManager(UUID managerId, UUID projectId);

    /**
     * @param projectId id of the project you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteProject(UUID projectId);

    /**
     * @param taskId id of the task you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteTask(UUID taskId);

    /**
     * @param bugReportId id of the bug report you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteBugReport(UUID bugReportId);

    /**
     * @param eventId id of the event you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteEvent(UUID eventId);

    /**
     * @param docId id of the documentation you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteDocumentation(UUID docId);

    /**
     * @param employeeId id of the employee you want to delete
     * @return Result with execution code and message if it fails
     */
    Result<NoData> deleteEmployee(UUID employeeId);


    /**
     * @param id id of the project
     * @return Result with Project, execution code and message if it fails
     */
    Result<Project> getProjectById(UUID id);


    /**
     * @param tags task tags for which tasks are selected
     * @param projectId id of the project
     * @return Result with ArrayList of tasks, execution code and message if it fails
     */
    Result<List<Task>> getTasksByTags(List<String> tags, UUID projectId);

    /**
     * @param projectId id of the project
     * @return Result with ArrayList of tasks, execution code and message if it fails
     */
    Result<List<Task>> getTasksByProjectId(UUID projectId);

    /**
     * @param employeeId id of the employee
     * @return Result with ArrayList of tasks, execution code and message if it fails
     */
    Result<List<Task>> getTasksByEmployeeId(UUID employeeId);

    /**
     * @param taskId id of the task you want to get by id
     * @return Result with Task, execution code and message if it fails
     */
    Result<Task> getTaskById(UUID taskId);

    /**
     * @param projectId id of project where bug reports are loaded from
     * @return Result with ArrayList of BugReport, execution code and message if it fails
     */
    Result<List<BugReport>> getBugReportsByProjectId(UUID projectId);

    /**
     * @param bugReportId id of BugReport you want to get
     * @return Result with BugReport, execution code and message if it fails
     */
    Result<BugReport> getBugReportById(UUID bugReportId);

    /**
     * @param projectId id of the project for which events are selected
     * @return Result with ArrayList of Event, execution code and message if it fails
     */
    Result<List<Event>> getEventsByProjectId(UUID projectId);

    /**
     * @param eventId id of the event
     * @return Result with Event, execution code and message if it fails
     */
    Result<Event> getEventById(UUID eventId);

    /**
     * @param projectId id of the project for which documentation is selected
     * @return Result with ArrayList of Documentation, execution code and message if it fails
     */
    Result<List<Documentation>> getDocumentationsByProjectId(UUID projectId);

    /**
     * @param docId id of the documentation
     * @return Result  with Documentation, execution code and message if it fails
     */
    Result<Documentation> getDocumentationById(UUID docId);

    /**
     * @param projectId id of project for which team is selected
     * @return Result with ArrayList of Employee, execution code and message if it fails
     */
    Result<List<Employee>> getProjectTeam(UUID projectId);

    /**
     * @param employeeId id of the employee whose data is being extracted
     * @return Result with Employee, execution code and message if it fails
     */
    Result<Employee> getEmployeeById(UUID employeeId);

    Result<NoData> completeTask(UUID taskId);

    default Result<NoData> initProjectEntities(Project project) {
        ArrayList<Result<NoData>> results = new ArrayList<>();

        project.getTeam().forEach(employee -> {
            Result<NoData> createEmployeeResult = processNewEmployee(employee);
            Result<NoData> bindEmployee = bindEmployeeToProject(employee.getId(), project.getId());
            results.addAll(List.of(createEmployeeResult, bindEmployee));

            if (project.getManager() != null && employee.getId().equals(project.getManager().getId())) {
                Result<NoData> bindProjectManagerResult = bindProjectManager(employee.getId(), project.getId());
                results.add(bindProjectManagerResult);
            }
        });


        results.addAll(project.getBugReports().stream()
                .map(this::processNewBugReport)
                .toList());

        results.addAll(project.getDocumentations().stream()
                .map(this::processNewDocumentation)
                .toList());

        results.addAll(project.getEvents().stream()
                .map(this::processNewEvent)
                .toList());

        results.addAll(project.getTasks().stream()
                .map(this::processNewTask)
                .toList());

        return results.stream()
                .filter(result -> result.getCode() != ResultCode.SUCCESS)
                .findAny()
                .orElse(new Result<>(ResultCode.SUCCESS));
    }
}
