package com.cog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CogApiApplication.class, args);
    }
    
//    @Bean
//    public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
//        return new WebMvcRegistrationsAdapter() {
//            @Override
//            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
//                return new RequestMappingHandlerMapping() {
//                    private final static String API_BASE_PATH = "api/1.0";
// 
//                    @Override
//                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
//                        Class<?> beanType = method.getDeclaringClass();
//                        if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
//                            PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH)
//                                    .combine(mapping.getPatternsCondition());
// 
//                            mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
//                                    mapping.getMethodsCondition(), mapping.getParamsCondition(),
//                                    mapping.getHeadersCondition(), mapping.getConsumesCondition(),
//                                    mapping.getProducesCondition(), mapping.getCustomCondition());
//                        }
// 
//                        super.registerHandlerMethod(handler, method, mapping);
//                    }
//                };
//            }
//        };
//    }
}