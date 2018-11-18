package com.funtime.bigdata.presto.aop;

import com.facebook.presto.jdbc.internal.jackson.databind.ObjectMapper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectMethodInterceptor implements MethodInterceptor {
    private Logger logger = LoggerFactory.getLogger(SelectMethodInterceptor.class);
    private final static ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        try {
            // proceed to original method call
            Object result = invocation.proceed();

            // same with AfterReturningAdvice
            System.out.println("InsertAroundMethod : after method inserted!");

            return result;

        } catch (IllegalArgumentException e) {
            // same with ThrowsAdvice
            System.out.println("InsertAroundMethod : Throw exception inserted!");
            throw e;
        }

//        Object[] args = invocation.getArguments();
//        Method method = invocation.getMethod();
//        Select select = AnnotationUtils.findAnnotation(method, Select.class);
////        System.out.println(select.value());
//        Type returnType = method.getGenericReturnType(); // 返回值类型
//        logger.info("返回值类型：" + returnType);
//        Type[] parammType = method.getGenericParameterTypes(); ///
//        for (Type type : parammType
//                ) {
//            logger.info("参数类型：" + type);
//        }
//        Parameter[] parameters = method.getParameters();
//        for (Parameter parameter : parameters
//                ) {
//            logger.info("参数：" + parameter);
//
//        }
////        return invocation.proceed();
//        return "";

//        logger.info("Before: interceptor name: {}", invocation.getMethod().getName());
//
//        logger.info("Arguments: {}", jsonMapper.writeValueAsString(invocation.getArguments()));
//
//        Object result = invocation.proceed();
//
//        logger.info("After: result: {}", jsonMapper.writeValueAsString(result));
//
//        return "";
    }

}
