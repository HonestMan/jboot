/**
 * Copyright (c) 2015-2018, Michael Yang 杨福海 (fuhai999@gmail.com).
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
package io.jboot.aop.interceptor.metric;


import com.codahale.metrics.Counter;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import io.jboot.Jboot;
import io.jboot.component.metric.annotation.EnableMetricCounter;
import io.jboot.utils.ClassKits;
import io.jboot.utils.StringUtils;

/**
 * 用于在AOP拦截，并通过Metrics的Conter进行统计
 */
public class JbootMetricCounterAopInterceptor implements Interceptor {

    private static final String suffix = ".counter";

    @Override
    public void intercept(Invocation inv) {

        EnableMetricCounter annotation = inv.getMethod().getAnnotation(EnableMetricCounter.class);

        if (annotation == null){
            inv.invoke();
            return;
        }

        Class targetClass = ClassKits.getUsefulClass(inv.getTarget().getClass());
        String name = StringUtils.isBlank(annotation.value())
                ? targetClass.getName() + "." + inv.getMethod().getName() + suffix
                : annotation.value();

        Counter counter = Jboot.me().getMetric().counter(name);
        counter.inc();
        inv.invoke();
    }
}
