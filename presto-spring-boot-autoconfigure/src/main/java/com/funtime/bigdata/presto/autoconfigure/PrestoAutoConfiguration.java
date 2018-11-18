package com.funtime.bigdata.presto.autoconfigure;

import com.funtime.bigdata.presto.annotations.Select;
import com.funtime.bigdata.presto.aop.SelectMethodInterceptor;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableConfigurationProperties(PrestoProperties.class)
public class PrestoAutoConfiguration extends AbstractPointcutAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(PrestoAutoConfiguration.class);

    @Autowired
    private PrestoProperties prestoProperties;

    @Bean
    PrestoJdbcUtils connectionFactory() {
        if (logger.isDebugEnabled()) {
            logger.debug("presto properties:{driver:" + prestoProperties.getDriver() + ",username:" +
                    prestoProperties.getUsername() + ",password:" +
                    prestoProperties.getPassword() + ",url:" + prestoProperties.getDriver() + "}");
        }
        return new PrestoJdbcUtils(prestoProperties.getDriver(), prestoProperties.getUsername(), prestoProperties
                .getPassword(), prestoProperties.getUrl());
    }

    private Pointcut pointcut;

    private Advice advice;

//    @Bean
//    BeanConfig createBeanConfig() {
//        return new BeanConfig();
//    }

    @PostConstruct
    public void selectInterceptorConfig() {
        logger.info("init LogAutoConfiguration start");
        this.pointcut = AnnotationMatchingPointcut.forMethodAnnotation(Select.class);
        this.advice = new SelectMethodInterceptor();
        logger.info("init LogAutoConfiguration end");
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Configuration
    @Import({AutoConfiguredMapperScannerRegistrar.class})
    @ConditionalOnMissingBean({PrestoRepositoryFactory.class})
    public static class MapperScannerRegistrarNotFoundConfiguration {
        public MapperScannerRegistrarNotFoundConfiguration() {
        }

        @PostConstruct
        public void afterPropertiesSet() {
            logger.debug("No {} found.", PrestoRepositoryFactory.class.getName());
        }
    }

    public static class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware,
            ImportBeanDefinitionRegistrar, ResourceLoaderAware {
        private BeanFactory beanFactory;
        private ResourceLoader resourceLoader;

        public AutoConfiguredMapperScannerRegistrar() {
        }

        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry
                registry) {
            ClassPathSelectAnnotationsScanner scanner = new ClassPathSelectAnnotationsScanner(registry);

            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }

                List ex = AutoConfigurationPackages.get(this.beanFactory);
//                scanner.setAnnotationClass(Mapper.class);
                scanner.registerFilters();
                scanner.doScan(StringUtils.toStringArray(ex));
            } catch (IllegalStateException var7) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.",
                        var7);
            }

        }

        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }


}
