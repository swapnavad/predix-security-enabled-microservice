package com.ge.predix.solsvc.boot;

import java.util.Arrays;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.StandardServletEnvironment;

@EnableAutoConfiguration(exclude =
{
        // Add any configuration loading call you want to exclude

})
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.ge.predix.solsvc")
@ImportResource({
    "classpath*:META-INF/application-security.xml"
})
@Controller
public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    
   
    /**
     * @param args
     *            -
     */
    @SuppressWarnings(
    {
    })
    public static void main(String[] args)
    {
        SpringApplication springApplication = new SpringApplication(Application.class);
        ApplicationContext ctx = springApplication.run(args);

        log.debug("Let's inspect the beans provided by Spring Boot:"); //$NON-NLS-1$
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames)
        {
            log.debug(beanName);
        }

        log.debug("Let's inspect the profiles provided by Spring Boot:"); //$NON-NLS-1$
        String profiles[] = ctx.getEnvironment().getActiveProfiles();
        for (int i = 0; i < profiles.length; i++)
            log.debug("profile=" + profiles[i]); //$NON-NLS-1$

        log.info("Let's inspect the properties provided by Spring Boot:"); //$NON-NLS-1$
        MutablePropertySources propertySources = ((StandardServletEnvironment) ctx.getEnvironment())
                .getPropertySources();
        Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources.iterator();
        while (iterator.hasNext())
        {
            Object propertySourceObject = iterator.next();
            if ( propertySourceObject instanceof org.springframework.core.env.PropertySource )
            {
                org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
                log.info("propertySource=" + propertySource.getName() + " values=" + propertySource.getSource() //$NON-NLS-1$ //$NON-NLS-2$
                        + "class=" + propertySource.getClass()); //$NON-NLS-1$
            }
        }
    }



    /**
     * Ensure the Tomcat container comes up, not the Jetty one.
     * 
     * @return - the factory
     */
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory()
    {
        return new TomcatEmbeddedServletContainerFactory();
    }
    

}
