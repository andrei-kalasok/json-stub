package nl.rabobank.powerofattorney.akalasok;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtility implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /*
        Get a class bean from the application context
     */
    public static <T> T getBean(final Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /*
        Return the application context if necessary for anything else
     */
    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        SpringUtility.applicationContext = applicationContext;
    }
}
