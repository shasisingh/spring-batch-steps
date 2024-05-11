package nl.nightcrawler.spring.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EntityScan(basePackages = {"nl.nightcrawler.spring.batch.domain"})
@ComponentScan(basePackages = {"nl.nightcrawler.spring.batch.mapper"})
@EnableJpaRepositories(basePackages = {"nl.nightcrawler.spring.batch.repositories"})
public class BatchTestConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/org/springframework/batch/core/schema-h2.sql")
                .addScript("init.sql")
                .build();
    }

    @Bean
    @Qualifier("transactionManager")
    public JpaTransactionManager transactionManager() {
        var jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("nl.nightcrawler.spring.batch.domain");
        factoryBean.setPersistenceUnitName("Employee");
        var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        return factoryBean;


    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        var factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setTransactionManager(transactionManager());
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();

    }

}
