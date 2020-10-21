package com.mvu.lottery.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mvu.lottery.customserializer.IFieldSerializerValueApprover;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;
import com.mvu.lottery.util.LoteryDataConverter;
import com.mvu.lottery.util.ProcessUtils;
import com.mysql.cj.jdbc.MysqlDataSource;

@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@PropertySource({ "classpath:application.properties", "classpath:externalprocess.properties",
		"classpath:mongodb.properties", "classpath:mysql.properties" })
@ComponentScan(basePackages = { "com.mvu.lottery.service" })
public class LotteryAppConfiguration {

	private Logger log = LoggerFactory.getLogger(LotteryAppConfiguration.class);

	/*
	 * @Autowired
	 * 
	 * @Bean public DataSource dataSource(MySqlDBConfig mySqlDbConfig) {
	 * 
	 * log.info(">>>>datasource()"); MysqlDataSource ds = new MysqlDataSource();
	 * 
	 * 
	 * 
	 * if (mySqlDbConfig == null) { log.info("this.mySqlDbConfig is null"); };
	 * 
	 * log.info(">>>dburl: "+mySqlDbConfig.getDbUrl());
	 * log.info(">>>user Name: "+mySqlDbConfig.getUserName());
	 * log.info(">>>pass: "+mySqlDbConfig.getPassword());
	 * 
	 * 
	 * ds.setURL(mySqlDbConfig.getDbUrl()); ds.setUser(mySqlDbConfig.getUserName());
	 * ds.setPassword(mySqlDbConfig.getPassword());
	 * 
	 * return (DataSource) ds;
	 * 
	 * 
	 * }
	 */

	@Bean
	public StrongPasswordEncryptor strongPasswordEncryptor() {

		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		return encryptor;
	}

	@Bean
	public PasswordEncoder passwordEncoder(StrongPasswordEncryptor passwordEncryptor) {

		PasswordEncoder passwordEncoder = new PasswordEncoder() {

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return passwordEncryptor.checkPassword(rawPassword.toString(), encodedPassword);

			}

			@Override
			public String encode(CharSequence rawPassword) {

				return passwordEncryptor.encryptPassword(rawPassword.toString());
			}
		};

		return passwordEncoder;
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);

		return provider;
	}

	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public LoteryDataConverter lotteryDataConverter() {
		return new LoteryDataConverter();
	};

	@Bean
	public ProcessUtils getProcessUtil() {
		return new ProcessUtils();
	}

	// @Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}

	/*
	 * @Autowired
	 * 
	 * @Bean public MongoDBData getMongoDBData(MongoDBConfig config) throws
	 * Exception {
	 * 
	 * return new MongoDBData(config.getHost(), config.getPort(),
	 * config.getDbName(), config.getCollectionName());
	 * 
	 * }
	 */

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcePlaceHolderConfiger() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public IFieldSerializerValueApprover createApprover() {
		return new IFieldSerializerValueApprover() {

			@Override
			public String filterName() {

				return "filteronmega";
			}

			@Override
			public boolean approve(Object pojo, String fieldName) {
				if (!"mega".equals(fieldName)) {
					return true;
				} else if (null == ((LastDrawnTicketStateHolder) pojo).getMega()) {
					return false;
				} else {
					return true;
				}

			}

		};
	}

	/**
	 * 
	 * @return
	 *//*
		 * private Properties jpaProperties() { Properties prop = new Properties();
		 * prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		 * return prop; }
		 */
	/**
	 * 
	 * @return
	 */
	/*
	 * @Bean(name = "entityManagerFactory") public EntityManagerFactory
	 * entityManagerFactory(DataSource mySqlDatasource) {
	 * 
	 * LocalContainerEntityManagerFactoryBean em = new
	 * LocalContainerEntityManagerFactoryBean(); em.setDataSource(mySqlDatasource);
	 * em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	 * em.setJpaProperties(this.jpaProperties()); em.setPackagesToScan(new String[]
	 * { "com.mvu.lottery.data.model" });
	 * em.setPersistenceUnitName("LotteryNumberPicker"); em.afterPropertiesSet();
	 * 
	 * return em.getObject(); }
	 */

	/**
	 * 
	 * @param emf
	 * @return
	 */
	/*
	 * @Bean public PlatformTransactionManager
	 * transacctionManager(EntityManagerFactory emf) {
	 * 
	 * System.out.println(">>>EntityManagerFactory:" + emf);
	 * 
	 * JpaTransactionManager transactionManager = new JpaTransactionManager();
	 * transactionManager.setEntityManagerFactory(emf);
	 * 
	 * return transactionManager;
	 * 
	 * }
	 */

}
