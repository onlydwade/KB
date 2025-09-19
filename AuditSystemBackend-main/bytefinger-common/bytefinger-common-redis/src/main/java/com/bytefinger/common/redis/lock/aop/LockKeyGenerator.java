/**
 * ﻿Copyright © 2018 organization 苞米豆
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytefinger.common.redis.lock.aop;

import com.bytefinger.common.redis.lock.annotation.DLock;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式锁Key生成器
 *
 * @author patrick
 * @since 1.0.0
 */
public class LockKeyGenerator {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    public String getKeyName(Method method, Object[] args, DLock dLock) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.hasLength(dLock.name()) && dLock.keys().length == 0) {
            throw new RuntimeException("分布式锁不能没有key");
        }
        if (StringUtils.hasLength(dLock.name())) {
            sb.append(dLock.name());
        }
        if (dLock.keys().length > 1 || !"".equals(dLock.keys()[0])) {
            sb.append(getSpelDefinitionKey(dLock.keys(), method, args));
        }
        return sb.toString();
    }


    private String getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, NAME_DISCOVERER);
        List<String> definitionKeyList = new ArrayList<>(definitionKeys.length);
        for (String definitionKey : definitionKeys) {
            if (definitionKey != null && !definitionKey.isEmpty()) {
                String key = PARSER.parseExpression(definitionKey).getValue(context).toString();
                definitionKeyList.add(key);
            }
        }
        return StringUtils.collectionToDelimitedString(definitionKeyList, ".", "", "");
    }

}
