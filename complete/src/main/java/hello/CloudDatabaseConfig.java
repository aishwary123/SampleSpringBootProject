package hello;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

//@Profile("cloud")
@Configuration
public class CloudDatabaseConfig{


		
		private static final Log log = LogFactory.getLog(CloudDatabaseConfig.class);
	
		
		@Profile("cloud")
		static class cloudDataSource extends AbstractCloudConfig{

//			@Value("${dsName}")
//			String dsName;
			
			
			@Bean
			public DataSource dataSource() {
				DataSource retVal = null;
				  try
				  {
					  
					  log.warn(new String("###############                  Setting Datasource using VCAP_SERVICES"));
						
					  List<String> dataSourceNames = Arrays.asList("BasicDbcpPooledDataSourceCreator",
				                "TomcatJdbcPooledDataSourceCreator", "HikariCpPooledDataSourceCreator",
				                "TomcatDbcpPooledDataSourceCreator");
				        DataSourceConfig dbConfig = new DataSourceConfig(dataSourceNames);
				        return connectionFactory().dataSource(dbConfig);
				  }
				  catch (CloudException ex)
				  {
				  ex.printStackTrace();
				  }
				  
				  return retVal;
				  	
			}
		}
		
		@Profile("default")
		static class localDataSource {
			
//			@Value("${dbURL}")
//			String dbURL;
//			
//			@Value("${dbUser}")
//			String dbUser;
//			
//			@Value("${dbPassword}")
//			String dbPassword;
//			
//			@Value("${dbDriver}")
//			String dbDriver;
//			
			@Bean
			public DataSource dataSource() {
				DataSource retVal = null;
				  try
				  {

//					  	DATABASE CONNECTION PARAMETERS FOR MySQL
//						DataSource ds=DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").url("jdbc:mysql://localhost:3306/sampleJPADB?useSSL=false").username("root").password("root").build();
						
//					  	DATABASE CONNECTION PARAMETERS FOR PostgreSQL
//					  	DataSource ds=DataSourceBuilder.create().driverClassName("org.postgresql.Driver").url("jdbc:postgresql://localhost:5432/test").username("testuser").password("test123!").build();
//						return ds;
					
				  }
				  catch (CloudException ex)
				  {
				  ex.printStackTrace();
				  }
				  
				  return retVal;
				  	
			}
		}
		
		

		@Bean
		public JpaVendorAdapter eclipseLink() {
			EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
			return adapter;
		}

		@Bean(name = "entityManagerFactory")
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter adapter, DataSource dataSource) {
			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setPackagesToScan("hello");
			factory.setPersistenceUnitName("SpringBootJPAWithEclipseLink");
			factory.setJpaVendorAdapter(eclipseLink());
			factory.setDataSource(dataSource);
			factory.setJpaProperties(getJpaProperties());
			log.warn("######   "+factory+"    "+factory.getPersistenceUnitName());
			return factory;
		}

		@Bean
		@Autowired
		public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
			log.warn("######  Entity Manager factory in Transaction Manager : "+emf);
			
			return new JpaTransactionManager(emf);
		}
		
		@Bean
		public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {
		    InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
		    return loadTimeWeaver;
		}
		
		public Properties getJpaProperties(){
			Properties properties = new Properties();       
//	        properties.put("eclipselink.deploy-on-startup", "true");
//	        properties.put("eclipselink.ddl-generation", "create-or-extend-tables");
//	        properties.put("eclipselink.ddl-generation.output-mode", "database");
//	        properties.put("eclipselink.weaving", "static");
//	        properties.put("eclipselink.weaving.lazy", "true");
//	        properties.put("eclipselink.weaving.internal", "true");
//	        properties.put("eclipselink.logging.level", "SEVERE");
//	        properties.put("eclipselink.query-results-cache.type", "WEAK");
//	        properties.put("eclipselink.jdbc.batch-writing", "JDBC");
//	        properties.put("eclipselink.jdbc.batch-writing.size", "1000");

	        properties.put("eclipselink.ddl-generation", "drop-and-create-tables");
	        properties.put("eclipselink.weaving", "false");

			return properties;
		}
	

}

