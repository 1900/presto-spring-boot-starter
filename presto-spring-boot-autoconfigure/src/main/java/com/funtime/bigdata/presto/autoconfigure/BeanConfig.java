package com.funtime.bigdata.presto.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanConfig implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ClassPathSelectAnnotationsScanner scanner = new ClassPathSelectAnnotationsScanner((BeanDefinitionRegistry)
                beanFactory);
        scanner.setResourceLoader(this.applicationContext);
        //scanner.scan("presto.demo");
    }
}
