package com.funtime.bigdata.presto.autoconfigure;

import com.funtime.bigdata.presto.repository.PrestoRepository;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

public class ClassPathSelectAnnotationsScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathSelectAnnotationsScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * 注册接口拦截器，继承PrestoRepository接口的接口将自动实例化
     */
    public void registerFilters() {
        this.addIncludeFilter(new AssignableTypeFilter(PrestoRepository.class) {
            protected boolean matchClassName(String className) {
                System.out.println("registerFilters:"+className);
                return false;
            }
        });
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
//            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
            definition.setBeanClass(PrestoRepositoryFactory.class);
        }
        return beanDefinitions;
    }

    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

}
