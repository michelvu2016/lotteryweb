package com.mvu.lottery.configuration;

import java.util.Properties;
import java.util.function.Supplier;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("usingh2")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
@ConfigurationProperties(prefix = "spring.user.h2.datasource")
public class H2DBConfig {

	private String dialect;
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public H2DBConfig() {
		// TODO Auto-generated constructor stub
	}

	@ConfigurationProperties(prefix = "spring.user.h2.datasource")
	@Bean("h2Datasource")
	public DataSource dataSource() {
		DataSourceBuilder builder = DataSourceBuilder.create();
		return builder
				.build();
	}
	
	@Bean("entityManagerFactory")
	@Qualifier("h2EntityManagerFactory")
	public EntityManagerFactory entityManagerFactory(@Qualifier("h2Datasource") DataSource dataSource) {
		
		
		Supplier<Properties> propFunc = () -> {
			return new Properties() {
				{
					setProperty("hibernate.dialect", dialect);
				}
			};
		};
		
		
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		emfb.setJpaProperties(propFunc.get());
		emfb.setPackagesToScan(new String[] {"com.mvu.lottery.data.model"});
		emfb.setPersistenceUnitName("LotteryNumberPicker");
		emfb.afterPropertiesSet();
		
		return emfb.getObject();
		
	}
	
	@Bean("platformTransactionManager")
	@Qualifier("h2platformTransactionManager")
	public PlatformTransactionManager platformTransactionManager(@Qualifier("h2EntityManagerFactory") EntityManagerFactory emf) {
		
		JpaTransactionManager transManager = new JpaTransactionManager();
				
		transManager.setEntityManagerFactory(emf);
		
		return transManager;
		
	}
	
}
