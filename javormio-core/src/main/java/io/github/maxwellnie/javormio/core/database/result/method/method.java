package io.github.maxwellnie.javormio.core.database.result.method;

import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;

import java.util.ArrayList;


/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public class method implements methodReflection{
    ArrayList<Reflection<?>> typeMappingsReflectionList = new ArrayList<>();

    /**
     * 根据类型映射获取对应的反射对象
     * 此方法旨在维护一个类型映射到反射对象的缓存，以提高类型查询的效率
     * 首先尝试从列表中查找是否已经存在该类型的反射对象，如果存在则直接返回
     * 如果不存在，则创建新的反射对象，并将其添加到列表中以供后续查询
     *
     * @param typeMapping 类型映射，用于标识和获取反射对象
     * @return 返回一个反射对象，该对象对应于给定的类型映射
     */
    @Override
    public Reflection<?> getReflection(TypeMapping typeMapping){
        // 检查给定的类型映射是否已经存在于列表中
        int flag = typeMappingsReflectionList.indexOf(typeMapping);
        if(flag != -1){
            // 如果存在，则直接返回对应的反射对象
            return typeMappingsReflectionList.get(flag);
        }else{
            // 如果不存在，则根据类型映射创建一个新的反射对象
            Reflection<?> reflection = typeMapping.getReflection();
            // 将新创建的反射对象添加到列表中，以供后续查询
            typeMappingsReflectionList.add(reflection);
            // 返回新创建的反射对象
            return reflection;
        }
    }

    @Override
    public void setNullReflection() {
        typeMappingsReflectionList.clear();
    }
}
