package io.github.maxwellnie.javormio.flexible.sql;

import io.github.maxwellnie.javormio.source.code.processor.Library;
import io.github.maxwellnie.javormio.source.code.processor.LibraryInitializationException;
import org.apache.velocity.app.Velocity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Maxwell Nie
 */
public class VelocityLibrary implements Library {
    @Override
    public void init() throws LibraryInitializationException {
        try{
            velocityInit();
        }catch (IOException e){
            throw new LibraryInitializationException("velocity init error:", e);
        }
    }

    @Override
    public String getName() {
        return "velocity";
    }

    /**
     * 初始化velocity
     */
    void velocityInit() throws IOException {
        //velocity配置文件
        Properties properties = new Properties();
        //加载velocity配置文件
        loadProperties(properties);
        //初始化velocity
        Velocity.init(properties);
    }

    /**
     * 应判断velocity.properties是否存在于classpath中，不存在就使用默认配置
     * @param properties
     */
    void loadProperties(Properties properties) throws IOException {
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("velocity.properties")) {
            if (in != null)
                properties.load(in);
            else{
                properties.setProperty("resource.loader", "class");
                properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            }
        }
    }
}
