package cool.zzy.sems.core.db.config;

import cool.zzy.sems.context.model.Config;
import cool.zzy.sems.core.db.mapper.ConfigMapper;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/31 14:49
 * @since 1.0
 */
public class GlobalConfig {
    private volatile Config config;

    public GlobalConfig(ConfigMapper configMapper) {
        config = configMapper.getActiveConfig();
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Integer getPasswordHashCount() {
        return this.config.getPasswordHashCount();
    }

    public String getRedisSignInUserPrefix() {
        return this.config.getRedisSignInUserPrefix();
    }

    public Integer getRedisSignInUserExpire() {
        return this.config.getRedisSignInUserExpire();
    }
}
