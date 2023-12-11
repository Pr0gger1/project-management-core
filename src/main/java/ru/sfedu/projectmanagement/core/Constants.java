package ru.sfedu.projectmanagement.core;

public class Constants {
    // property file formats
    public static final String DEFAULT_CONFIG_PATH_PROPERTIES = "env.properties";
    public static final String DEFAULT_CONFIG_PATH_XML = "env.xml";
    public static final String DEFAULT_CONFIG_PATH_YML = "env.yml";


    // file database extensions
    public static final String FILE_XML_EXTENSION = ".xml";
    public static final String FILE_CSV_EXTENSION = ".csv";
    public static final String CSV_DEFAULT_SEPARATOR = ";";


    // xml and csv entity filenames
    public static final String PROJECTS_FILE_PATH = "projects";
    public static final String EMPLOYEES_FILE_PATH = "employees";
    public static final String TASKS_FILE_PATH = "tasks";
    public static final String BUG_REPORTS_FILE_PATH = "bug_reports";
    public static final String EVENTS_FILE_PATH = "events";
    public static final String DOCUMENTATIONS_FILE_PATH = "documentations";
    public static final String EMPLOYEE_PROJECT_FILE_PATH = "employee_project";


    // mongo history collection item name constants
    public static final String MONGO_HISTORY_ID = "Id";
    public static final String MONGO_HISTORY_CLASSNAME = "Class name";
    public static final String MONGO_HISTORY_CREATED_AT = "Created at";
    public static final String MONGO_HISTORY_ACTOR = "System";
    public static final String MONGO_HISTORY_METHOD_NAME = "Method name";
    public static final String MONGO_HISTORY_OBJECT = "Object";
    public static final String MONGO_HISTORY_STATUS = "Status";
    public static final String MONGO_HISTORY_CHANGE_TYPE = "Change type";

    // mongo database names
    public static final String MONGO_DB_NAME_TEST = "history_test";
    public static final String MONGO_DB_NAME_PRODUCTION = "history";


    // datasource path
    public static final String DATASOURCE_PATH_XML = "data/xml/";
    public static final String DATASOURCE_PATH_CSV = "data/csv/";
    public static final String DATASOURCE_TEST_PATH_XML = "src/test/data/xml/";
    public static final String DATASOURCE_TEST_PATH_CSV = "src/test/data/csv/";

    // properties variables
    public static final String MONGO_URL = "MONGO_URL";
    public static final String POSTGRES_URL = "POSTGRES_URL";
    public static final String POSTGRES_USER = "POSTGRES_USER";
    public static final String POSTGRES_PASSWORD = "POSTGRES_PASSWORD";
    public static final String POSTGRES_PROD_DB_NAME = "POSTGRES_PROD_DB_NAME";
    public static final String POSTGRES_TEST_DB_NAME = "POSTGRES_TEST_DB_NAME";
    public static final String ENVIRONMENT = "ENVIRONMENT";
    public static final String DATABASE_TYPE = "DATABASE_TYPE";



    public static final String TRACK_INFO_KEY_LABOR_EFFICIENCY = "labor_efficiency";
    public static final String TRACK_INFO_KEY_PROJECT_READINESS = "project_readiness";
    public static final String TRACK_INFO_KEY_TASK_STATUS = "task_status";
    public static final String TRACK_INFO_KEY_BUG_STATUS = "bug_status";
}