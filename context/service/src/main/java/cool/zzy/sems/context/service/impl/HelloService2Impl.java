package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.service.HelloService;
import cool.zzy.sems.rpc.common.annotation.RpcService;

/**
 * @author intent
 */
@RpcService(value = HelloService.class, version = 2)
public class HelloService2Impl implements HelloService {

    public HelloService2Impl() {
    }

    @Override
    public String hello(String name) {
        return "Hi " + name;
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age + " years old";
    }
}
