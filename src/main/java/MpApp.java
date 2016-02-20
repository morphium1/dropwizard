import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import mp.app.dao.user.AccountDao;
import mp.app.dao.user.UserDao;
import mp.app.filter.LoggingFilter;
import mp.app.filter.RequestIdHeaderFilter;
import mp.app.health.MongoHealthCheck;
import mp.app.resources.AccountResource;
import mp.app.resources.UserResource;
import mp.app.service.user.AccountServiceImpl;
import mp.app.service.user.UserServiceImpl;
import mp.app.startup.HealthcheckStartup;
import mp.app.tasks.MyTask;

import java.util.logging.Logger;


public class MpApp extends Application<MpAppConfiguration> {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public static void main(String[] args) throws Exception{
        new MpApp().run(args);
    }

    @Override
    public void run(MpAppConfiguration configuration, Environment environment) throws Exception {
        // Filter
        environment.jersey().register(new RequestIdHeaderFilter());
        environment.jersey().register(new LoggingFilter(logger));

        MongoClient mongoClient = configuration.getMongoClientFactory().build();

        // Daos
        UserDao userDao = new UserDao(mongoClient, environment.getObjectMapper());
        AccountDao accountDao = new AccountDao(mongoClient, environment.getObjectMapper());

        // Service
        UserServiceImpl userService = new UserServiceImpl(userDao);
        AccountServiceImpl accountService = new AccountServiceImpl(accountDao);

        // Tasks
        environment.admin().addTask(new MyTask());

        // Resources
        environment.jersey().register(new UserResource(userService));
        environment.jersey().register(new AccountResource(accountService));

        // Healthchecks
        environment.healthChecks().register(MongoHealthCheck.NAME, new MongoHealthCheck(mongoClient));

        // Managed
        environment.lifecycle().manage(new HealthcheckStartup(environment.healthChecks()));

    }

    @Override
    public void initialize(Bootstrap<MpAppConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/","index.html"));
        configureObjectMapper(bootstrap.getObjectMapper());
    }

    private void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
