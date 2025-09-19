package com.bytefinger.common.security.utils;

import cn.hutool.core.util.ObjectUtil;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.security.annotation.Dict;
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
public class DictSerializer extends StdSerializer<Object> implements ContextualSerializer {

    /**
     * 字典注解
     */
    private Dict dict;

    public DictSerializer() {
        super(Object.class);
    }

    public DictSerializer(Dict dict) {
        super(Object.class);
        this.dict = dict;
    }

    private String type;
    private String ptype;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) {

        try {
            if (Objects.isNull(value)) {
                gen.writeObject(value);
                return;
            }

            if (Objects.nonNull(dict)) {
                type = dict.type();
                ptype = dict.ptype();
            }


            // 通过数据字典类型和value获取name
            String label = null;

            if (StringUtils.isNotEmpty(ptype)) {


                if (StringUtils.isNotEmpty(value.toString()) && value.toString().contains(",")){

                    String[] split = value.toString().split(",");
                    String sonLabel="";
                    for (int i = 0; i < split.length; i++) {
                        String dictPdataOptions = DictUtils.getDictPdataOptions(ptype, split[i]);
                        if (StringUtils.isNotEmpty(dictPdataOptions)){
                            sonLabel = sonLabel+dictPdataOptions+",";
                        }

                    }
                    label = sonLabel.substring(0, sonLabel.length()-1);
                }else {
                    label = DictUtils.getDictPdataOptions(ptype, value.toString());
                }
                if (ptype.contains(",") && StringUtils.isNotEmpty(value.toString())){

                    String[] split = ptype.split(",");
                    String sonLabel="";
                    for (int i = 0; i < split.length; i++) {
                        String dictPdataOptions = DictUtils.getDictPdataOptions(split[i], value.toString());
                        if (StringUtils.isNotEmpty(dictPdataOptions)){
                            sonLabel = sonLabel+dictPdataOptions+",";
                        }

                    }
                    if (ObjectUtil.isNotEmpty(sonLabel)){
                        label = sonLabel.substring(0, sonLabel.length()-1);
                    }

                }


            } else {

                if (StringUtils.isNotEmpty(value.toString()) && value.toString().contains(",")){

                    String[] split = value.toString().split(",");
                    String sonLabel="";
                    for (int i = 0; i < split.length; i++) {

                        String dictDataOptions = DictUtils.getDictDataOptions(type, split[i]);
                        if (StringUtils.isNotEmpty(dictDataOptions)){
                            sonLabel = sonLabel+dictDataOptions+",";
                        }

                    }
                    label = sonLabel.substring(0, sonLabel.length()-1);

                }else {
                    label = DictUtils.getDictDataOptions(type, value.toString());
                }
            }

            gen.writeObject(value);
            gen.writeFieldName(gen.getOutputContext().getCurrentName() + "Str");
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

        Dict dict = beanProperty.getAnnotation(Dict.class);
        if (Objects.nonNull(dict)) {
            type = dict.type();
            ptype = dict.ptype();
            return this;
        }

        return prov.findNullValueSerializer(null);
    }
}
