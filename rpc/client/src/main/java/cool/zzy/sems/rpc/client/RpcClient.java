package cool.zzy.sems.rpc.client;

import cool.zzy.sems.rpc.client.core.ConnectionManager;
import cool.zzy.sems.rpc.client.proxy.ObjectProxy;
import cool.zzy.sems.rpc.common.annotation.RpcAutowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 10:35
 * @since 1.0
 */
public class RpcClient implements ApplicationContextAware, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public RpcClient(String address) {
        String[] array = address.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        ConnectionManager.getInstance().connectServerNode(host, port);
    }

    public <T> T createService(Class<T> interfaceClass, int version) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy<T>(interfaceClass, version));
    }

    public void stop() {
        ConnectionManager.getInstance().stop();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = ctx.getBean(beanName);
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    RpcAutowired rpcAutowired = field.getAnnotation(RpcAutowired.class);
                    if (rpcAutowired != null) {
                        int version = rpcAutowired.version();
                        field.setAccessible(true);
                        field.set(bean, createService(field.getType(), version));
                    }
                } catch (Exception e) {
                    logger.info("{}", e.getMessage());
                }
            }
        }
    }
}
