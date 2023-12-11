package ru.sfedu.projectmanagement.core.api;

import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.projectmanagement.core.model.*;
import ru.sfedu.projectmanagement.core.utils.config.ConfigPropertiesUtil;
import ru.sfedu.projectmanagement.core.utils.types.Result;
import ru.sfedu.projectmanagement.core.utils.types.TrackInfo;
import ru.sfedu.projectmanagement.core.utils.xml.Wrapper;
import ru.sfedu.projectmanagement.core.Constants;
import ru.sfedu.projectmanagement.core.utils.ResultCode;
import ru.sfedu.projectmanagement.core.utils.xml.XmlUtil;

import static ru.sfedu.projectmanagement.core.utils.FileUtil.createFileIfNotExists;
import static ru.sfedu.projectmanagement.core.utils.FileUtil.createFolderIfNotExists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class XmlDataProvider extends DataProvider {
    private final Logger logger = LogManager.getLogger(XmlDataProvider.class);
    private final String projectsFilePath;
    private final String employeesFilePath;
    private final String tasksFilePath;
    private final String bugReportsFilePath;
    private final String eventsFilePath;
    private final String documentationsFilePath;
    private final String employeeProjectFilePath;


    public XmlDataProvider() {
        Environment environment = Environment.valueOf(ConfigPropertiesUtil.getEnvironmentVariable(Constants.ENVIRONMENT));
        String actualDatasourcePath = environment == Environment.PRODUCTION ?
                Constants.DATASOURCE_PATH_XML :
                Constants.DATASOURCE_TEST_PATH_XML;

        projectsFilePath = actualDatasourcePath
                .concat(Constants.PROJECTS_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        employeesFilePath = actualDatasourcePath
                .concat(Constants.EMPLOYEES_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        tasksFilePath = actualDatasourcePath
                .concat(Constants.TASKS_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        bugReportsFilePath = actualDatasourcePath
                .concat(Constants.BUG_REPORTS_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        eventsFilePath = actualDatasourcePath
                .concat(Constants.EVENTS_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        documentationsFilePath = actualDatasourcePath
                .concat(Constants.DOCUMENTATIONS_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        employeeProjectFilePath = actualDatasourcePath
                .concat(Constants.EMPLOYEE_PROJECT_FILE_PATH)
                .concat(Constants.FILE_XML_EXTENSION);

        try {
            createFolderIfNotExists(actualDatasourcePath);
            createDatasourceFiles();
        }
        catch (IOException exception) {
            logger.error("Database initialization error: {}", exception.getMessage());
        }
    }

    private void createDatasourceFiles() throws IOException {
        createFileIfNotExists(projectsFilePath);
        createFileIfNotExists(employeesFilePath);
        createFileIfNotExists(employeeProjectFilePath);
        createFileIfNotExists(tasksFilePath);
        createFileIfNotExists(bugReportsFilePath);
        createFileIfNotExists(eventsFilePath);
        createFileIfNotExists(documentationsFilePath);
    }

    /**
     * @param project
     * @return
     */
    @Override
    public Result<?> processNewProject(Project project) {
        try {
            XmlUtil.createRecord(projectsFilePath, project);
            logger.debug("processNewProject[1]: project was written in xml {}", project);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processNewProject[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }

    }

    /**
     * @param task
     * @return
     */
    @Override
    public Result<?> processNewTask(Task task) {
        try {
            XmlUtil.createRecord(tasksFilePath, task);
            logger.debug("processNewTask[1]: task was written in xml {}", task);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processNewTask[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }
    }

    /**
     * @param bugReport
     * @return
     */
    @Override
    public Result<?> processNewBugReport(BugReport bugReport) {
        try {
            XmlUtil.createRecord(bugReportsFilePath, bugReport);
            logger.debug("processBugReport[1]: bug report was written in xml {}", bugReport);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processBugReport[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }
    }

    /**
     * @param documentation
     * @return
     */
    @Override
    public Result<?> processNewDocumentation(Documentation documentation) {
        try {
            XmlUtil.createRecord(documentationsFilePath, documentation);
            logger.debug("processNewDocumentation[1]: documentation was written in xml {}", documentation);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processNewDocumentation[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }
    }

    @Override
    public Result<?> processNewEmployee(Employee employee) {
        try {
            XmlUtil.createRecord(employeesFilePath, employee);
            logger.debug("processNewEmployee[1]: employee was written in xml {}", employee);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processNewEmployee[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }
    }

    /**
     * @param event
     * @return
     */
    @Override
    public Result<?> processNewEvent(Event event) {
        try {
            XmlUtil.createRecord(eventsFilePath, event);
            logger.debug("processNewEvent[1]: task was written in xml {}", event);
            return new Result<>(ResultCode.SUCCESS);
        }
        catch (JAXBException exception) {
            logger.error("processNewEvent[2]: {}", exception.getMessage());
            return new Result<>(ResultCode.ERROR, exception.getMessage());
        }
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<Project> getProjectById(String projectId) {
        Wrapper<Project> projectWrapper = XmlUtil.read(projectsFilePath);
       return projectWrapper.getList()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .map(p -> {
                    logger.debug("getProjectById[1]: received project {}", p);
                    return new Result<>(p, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getProjectById[2]: project with id {} was not found", projectId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });
    }

    /**
     * @param taskId
     * @return
     */
    @Override
    public Result<Task> getTaskById(UUID taskId) {
        Wrapper<Task> taskWrapper = XmlUtil.read(tasksFilePath);
        return taskWrapper.getList()
                .stream()
                .filter(t -> t.getId().equals(taskId))
                .map(t -> {
                    logger.debug("getTaskById[1]: received task {}", t);
                    return new Result<>(t, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getTaskById[2]: task with id {} was not found", taskId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });

    }

    /**
     * @param bugReportId
     * @return
     */
    @Override
    public Result<BugReport> getBugReportById(UUID bugReportId) {
        Wrapper<BugReport> bugReportWrapper = XmlUtil.read(bugReportsFilePath);
        return bugReportWrapper.getList()
                .stream()
                .filter(bg -> bg.getId().equals(bugReportId))
                .map(bg -> {
                    logger.debug("getBugReportById[1]: received bug report {}", bg);
                    return new Result<>(bg, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getBugReportsById[2]: bug report with id {} was not found", bugReportId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });
    }

    /**
     * @param docId
     * @return
     */
    @Override
    public Result<Documentation> getDocumentationById(UUID docId) {
        Wrapper<Documentation> documentationWrapper = XmlUtil.read(documentationsFilePath);
        return documentationWrapper.getList()
                .stream()
                .filter(doc -> doc.getId().equals(docId))
                .map(doc -> {
                    logger.debug("getDocumentationById[1]: received documentation {}", doc);
                    return new Result<>(doc, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getDocumentationById[2]: documentation with id {} was not found", docId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });
    }

    /**
     * @param eventId
     * @return
     */
    @Override
    public Result<Event> getEventById(UUID eventId) {
        Wrapper<Event> eventWrapper = XmlUtil.read(eventsFilePath);
        return eventWrapper.getList()
                .stream()
                .filter(event -> event.getId().equals(eventId))
                .map(event -> {
                    logger.debug("getEventById[1]: received event {}", event);
                    return new Result<>(event, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getEventById[2]: event with id {} was not found", eventId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });
    }

    /**
     * @param employeeId
     * @return
     */
    @Override
    public Result<Employee> getEmployeeById(UUID employeeId) {
        Wrapper<Employee> employeeWrapper = XmlUtil.read(employeesFilePath);
        return employeeWrapper.getList()
                .stream()
                .filter(employee -> employee.getId().equals(employeeId))
                .map(employee -> {
                    logger.debug("getEmployeeById[1]: received employee {}", employee);
                    return new Result<>(employee, ResultCode.SUCCESS);
                })
                .findFirst()
                .orElseGet(() -> {
                    logger.debug("getEmployeeById[2]: employee with id {} was not found", employeeId);
                    return new Result<>(ResultCode.NOT_FOUND);
                });
    }

    /**
     * @param tags
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<Task>> getTasksByTags(ArrayList<String> tags, String projectId) {
        Wrapper<Task> taskWrapper = XmlUtil.read(tasksFilePath);
        ArrayList<Task> tasks = taskWrapper.getList()
                .stream()
                .filter(task -> task.getTags().containsAll(tags))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (tasks.isEmpty())
            return new Result<>(tasks, ResultCode.NOT_FOUND);
        return new Result<>(tasks, ResultCode.SUCCESS);
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<Task>> getTasksByProjectId(String projectId) {
        Wrapper<Task> taskWrapper = XmlUtil.read(tasksFilePath);
        ArrayList<Task> tasks = taskWrapper.getList()
                .stream()
                .filter(task -> task.getProjectId().equals(projectId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (tasks.isEmpty())
            return new Result<>(tasks, ResultCode.NOT_FOUND);
        return new Result<>(tasks, ResultCode.SUCCESS);
    }

    /**
     * @param employeeId
     * @return
     */
    @Override
    public Result<ArrayList<Task>> getTasksByEmployeeId(UUID employeeId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<BugReport>> getBugReportsByProjectId(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<Event>> getEventsByProjectId(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<Documentation>> getDocumentationsByProjectId(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<ArrayList<Employee>> getProjectTeam(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @param checkLaborEfficiency
     * @param trackBugs
     * @return
     */
    @Override
    public TrackInfo<String, ?> monitorProjectCharacteristics(String projectId, boolean checkLaborEfficiency, boolean trackBugs) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    protected float calculateProjectReadiness(String projectId) {
        return 0;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    protected TrackInfo<Employee, Float> calculateLaborEfficiency(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    protected TrackInfo<Task, String> trackTaskStatus(String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    protected TrackInfo<BugReport, String> trackBugReportStatus(String projectId) {
        return null;
    }

    /**
     * @param employeeId
     * @param projectId
     * @return
     */
    @Override
    public Result<?> bindEmployeeToProject(UUID employeeId, String projectId) {
        return null;
    }

    /**
     * @param managerId
     * @param projectId
     * @return
     */
    @Override
    public Result<?> bindProjectManager(UUID managerId, String projectId) {
        return null;
    }

    /**
     * @param executorId
     * @param executorFullName
     * @param taskId
     * @param projectId
     * @return
     */
    @Override
    public Result<?> bindTaskExecutor(UUID executorId, String executorFullName, UUID taskId, String projectId) {
        return null;
    }

    /**
     * @param projectId
     * @return
     */
    @Override
    public Result<?> deleteProject(String projectId) {
        return null;
    }

    /**
     * @param taskId
     * @return
     */
    @Override
    public Result<?> deleteTask(UUID taskId) {
        return null;
    }

    /**
     * @param bugReportId
     * @return
     */
    @Override
    public Result<?> deleteBugReport(UUID bugReportId) {
        return null;
    }

    /**
     * @param eventId
     * @return
     */
    @Override
    public Result<?> deleteEvent(UUID eventId) {
        return null;
    }

    /**
     * @param docId
     * @return
     */
    @Override
    public Result<?> deleteDocumentation(UUID docId) {
        return null;
    }

    /**
     * @param employeeId
     * @return
     */
    @Override
    public Result<?> deleteEmployee(UUID employeeId) {
        return null;
    }
}