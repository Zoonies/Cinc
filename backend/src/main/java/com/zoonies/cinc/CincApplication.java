package com.zoonies.cinc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zoonies.cinc.cli.RenderCommand;
import com.zoonies.cinc.core.MeasuredDoubleEvent;
import com.zoonies.cinc.resources.ApiResource;
import com.zoonies.cinc.resources.JodaDateTimeJsonDeserializer;
import com.zoonies.cinc.resources.JodaDateTimeJsonSerializer;
import com.zoonies.cinc.resources.PojoMessageBodyReader;
import com.zoonies.cinc.resources.PojoMessageBodyWriter;

public class CincApplication extends Application<CincConfiguration> {
  public static void main(String[] args) throws Exception {
    new CincApplication().run(args);
  }

  @Override
  public String getName() {
    return "cinc";
  }

  @Override
  public void initialize(Bootstrap<CincConfiguration> bootstrap) {
    bootstrap.addCommand(new RenderCommand());
    bootstrap.addBundle(new AssetsBundle());
    bootstrap.addBundle(new MigrationsBundle<CincConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(CincConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });
    bootstrap.addBundle(new ViewBundle());

  }

  @Override
  public void run(CincConfiguration configuration, Environment environment)
      throws ClassNotFoundException {
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "jdbiDb");
    jdbi.registerMapper(new ResultSetMapper<MeasuredDoubleEvent>() {
      @Override
      public MeasuredDoubleEvent map(int index, ResultSet r, StatementContext ctx)
          throws SQLException {
        Date date = r.getDate("dateTime");
        DateTime realDate = new DateTime(date.getTime());
        double measure = r.getDouble("measure");
        return new MeasuredDoubleEvent(realDate, measure);
      }
    });
    
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(DateTime.class, new JodaDateTimeJsonSerializer());
    simpleModule.addDeserializer(DateTime.class, new JodaDateTimeJsonDeserializer());
    objectMapper.registerModule(simpleModule);
    
    environment.jersey().register(new ApiResource(jdbi));
    environment.jersey().register(new PojoMessageBodyWriter(objectMapper));
    environment.jersey().register(new PojoMessageBodyReader(objectMapper));
  }
}
