package com.bytefinger.common.security.utils;


import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.annotation.Cnarea;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 字典
 *
 * @author pat
 * @date 2022/10/22 02:17
 **/
@Slf4j
public class CnareaSerializer extends StdSerializer<Object> implements ContextualSerializer {

    /**
     * 注解
     */
    private Cnarea cnarea;

    public CnareaSerializer() {
        super(Object.class);
    }

    public CnareaSerializer(Cnarea cnarea) {
        super(Object.class);
        this.cnarea = cnarea;
    }

    private CnareaEnum type;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) {

        try {
            if (Objects.isNull(value)) {
                gen.writeObject(value);
                return;
            }

            if (Objects.nonNull(cnarea)) {
                type = cnarea.type();
            }

            // 通过数据字典类型和value获取name
            String label = CnareaUtils.getOptions(type.name(), value.toString());
            gen.writeObject(value);
            gen.writeFieldName(gen.getOutputContext().getCurrentName().replace("Code", "Name"));
            gen.writeObject(label);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty beanProperty) throws JsonMappingException {
        if (Objects.isNull(beanProperty)) {
            return prov.findValueSerializer(beanProperty.getType(), beanProperty);
        }

        Cnarea cnarea = beanProperty.getAnnotation(Cnarea.class);
        if (Objects.nonNull(cnarea)) {
            type = cnarea.type();
            return this;
        }

        return prov.findNullValueSerializer(null);
    }
}
