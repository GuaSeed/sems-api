package cool.zzy.sems.context.service.impl;

import cool.zzy.sems.context.service.HelloService;
import cool.zzy.sems.rpc.common.annotation.RpcService;

/**
 * @author intent
 */
@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl() {
    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }
}
