/**
 * Copyright 2009-2015 the original author or authors.
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
package com.snail.source.mybatis.utils;

/**
 * @author Clinton Begin
 */
public interface TokenHandler {

    /**
     * 解析占位符。context是参数名称，如 #{id} 的 id。
     *
     * @param content 内容
     * @return {@link String }
     */
    String handleToken(String content);

}

