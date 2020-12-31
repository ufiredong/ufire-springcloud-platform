//package com.springcloud.ufire.conf;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.PropertyKeyConst;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.springcloud.ufire.entiy.FilterEntity;
//import com.springcloud.ufire.entiy.PredicateEntity;
//import com.springcloud.ufire.entiy.RouteEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
//import org.springframework.cloud.gateway.filter.FilterDefinition;
//import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;
//import reactor.core.publisher.Mono;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//
///**
// * @program: ufire-springcloud-platform
// * @description: X
// * @author: fengandong
// * @create: 2020-12-30 18:08
// **/
//@Component
//public class DynamicRoutingConfig implements ApplicationEventPublisherAware {
//
//    private final Logger logger = LoggerFactory.getLogger(DynamicRoutingConfig.class);
//
//    private static final String DATA_ID = "getway.json";
//    private static final String Group = "DEFAULT_GROUP";
//
//    @Autowired
//    private RouteDefinitionWriter routeDefinitionWriter;
//
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @Bean
//    public void refreshRouting() throws NacosException {
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, "127.0.0.1:8848");
//        properties.put(PropertyKeyConst.NAMESPACE, "8282c713-da90-486a-8438-2a5a212ef44f");
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        configService.addListener(DATA_ID, Group, new Listener() {
//            @Override
//            public Executor getExecutor() {
//                return null;
//            }
//
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                logger.info(configInfo);
//
//                boolean refreshGatewayRoute = JSONObject.parseObject(configInfo).getBoolean("refreshGatewayRoute");
//
//                if (refreshGatewayRoute) {
//                    List<RouteEntity> list = JSON.parseArray(JSONObject.parseObject(configInfo).getString("routeList")).toJavaList(RouteEntity.class);
//
//                    for (RouteEntity route : list) {
//                        update(assembleRouteDefinition(route));
//                    }
//                } else {
//                    logger.info("路由未发生变更");
//                }
//
//
//            }
//        });
//    }
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.applicationEventPublisher = applicationEventPublisher;
//    }
//
//    /**
//     * 路由更新
//     * @param routeDefinition
//     * @return
//     */
//    public void update(RouteDefinition routeDefinition){
//
//        try {
//            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
//            logger.info("路由更新成功");
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//        }
//
//        try {
//            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
//            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
//            logger.info("路由更新成功");
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//        }
//    }
//
//    public RouteDefinition assembleRouteDefinition(RouteEntity routeEntity) {
//
//        RouteDefinition definition = new RouteDefinition();
//
//        // ID
//        definition.setId(routeEntity.getId());
//
//        // Predicates
//        List<PredicateDefinition> pdList = new ArrayList<>();
//        for (PredicateEntity predicateEntity: routeEntity.getPredicates()) {
//            PredicateDefinition predicateDefinition = new PredicateDefinition();
//            predicateDefinition.setArgs(predicateEntity.getArgs());
//            predicateDefinition.setName(predicateEntity.getName());
//            pdList.add(predicateDefinition);
//        }
//        definition.setPredicates(pdList);
//
//        // Filters
//        List<FilterDefinition> fdList = new ArrayList<>();
//        for (FilterEntity filterEntity: routeEntity.getFilters()) {
//            FilterDefinition filterDefinition = new FilterDefinition();
//            filterDefinition.setArgs(filterEntity.getArgs());
//            filterDefinition.setName(filterEntity.getName());
//            fdList.add(filterDefinition);
//        }
//        definition.setFilters(fdList);
//
//        // URI
//        URI uri = UriComponentsBuilder.fromUriString(routeEntity.getUri()).build().toUri();
//        definition.setUri(uri);
//
//        return definition;
//    }
//}