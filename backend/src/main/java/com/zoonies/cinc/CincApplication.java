package com.zoonies.cinc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.DBI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zoonies.cinc.cli.RenderCommand;
import com.zoonies.cinc.db.MeasuredDoubleEventMapper;
import com.zoonies.cinc.db.MeasuredIntegerEventMapper;
import com.zoonies.cinc.db.MeasuredStringEventMapper;
import com.zoonies.cinc.db.StreamInfoMapper;
import com.zoonies.cinc.resources.JodaDateTimeJsonDeserializer;
import com.zoonies.cinc.resources.JodaDateTimeJsonSerializer;
import com.zoonies.cinc.resources.PojoMessageBodyReader;
import com.zoonies.cinc.resources.PojoMessageBodyWriter;
import com.zoonies.cinc.resources.StreamsResource;
import com.zoonies.cinc.resources.UsersResource;

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
    jdbi.registerMapper(new MeasuredDoubleEventMapper());
    jdbi.registerMapper(new MeasuredIntegerEventMapper());
    jdbi.registerMapper(new MeasuredStringEventMapper());
    jdbi.registerMapper(new StreamInfoMapper());
    
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(DateTime.class, new JodaDateTimeJsonSerializer());
    simpleModule.addDeserializer(DateTime.class, new JodaDateTimeJsonDeserializer());
    objectMapper.registerModule(simpleModule);
    
    environment.jersey().register(new UsersResource(jdbi));
    environment.jersey().register(new StreamsResource(jdbi));
    environment.jersey().register(new PojoMessageBodyWriter(objectMapper));
    environment.jersey().register(new PojoMessageBodyReader(objectMapper));
  }
}
