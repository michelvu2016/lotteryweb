package com.mvu.lottery.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("usingMysql")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mysqlEntityManagerFactory", transactionManagerRef = "mysqlTranactionManager")
@ConfigurationProperties(prefix = "spring.user.mysql.datasource")
public class MySqlDSConfig {

	private String dialect;

	public MySqlDSConfig() {

	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	// Define the datasource
	@Bean(name = "mySqlDataSource")
	@ConfigurationProperties(prefix = "spring.user.mysql.datasource")
	public DataSource dataSource() {
		DataSourceBuilder builder = DataSourceBuilder.create();
		return builder.build();
	}

	@Bean("mysqlEntityManagerFactory")
	public EntityManagerFactory entityManagerFatory(@Qualifier("mySqlDataSource") DataSource dataSource,
			EntityManagerFactoryBuilder entityFactoryBuilder) {

		/*
		 * LocalContainerEntityManagerFactoryBean emfBean = entityFactoryBuilder
		 * .dataSource(dataSource) .persistenceUnit("MYSQL") .packages(new String[]
		 * {"com.mvu.lottery.data.model"})
		 * 
		 * .properties(jpaProperties())
		 * 
		 * .build();
		 * 
		 * emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		 * 
		 * return emfBean.getObject();
		 */

		
		  LocalContainerEntityManagerFactoryBean em = new
		  LocalContainerEntityManagerFactoryBean(); em.setDataSource(dataSource);
		  em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		  em.setJpaProperties(this.jpaProperties()); em.setPackagesToScan(new
				  	String[] { "com.mvu.lottery.data.model" });
		  em.setPersistenceUnitName("LotteryNumberPicker"); em.afterPropertiesSet();
		  
		  return em.getObject();
		 
	}

	@Bean(name = "mysqlTranactionManager")
	public PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/**
	 * 
	 * @return
	 */
	private Properties jpaProperties() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		return prop;
	}

	
	/**
	 * 
	 * @return
	 */
	private Map<String, String> jpaPropertiesMap() {
		Map<String, String> prop = new LinkedHashMap<>();

		prop.put("hibernate.dialect", this.dialect);
		return prop;
	}

}
