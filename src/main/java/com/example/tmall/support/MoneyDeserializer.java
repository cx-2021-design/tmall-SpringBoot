package com.example.tmall.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;

@JsonComponent//将 MoneyDeserializer 类标记为一个Jackson组件
public class MoneyDeserializer extends StdDeserializer<Money> {
    protected MoneyDeserializer() {
        super(Money.class);
    }

    /**
     * 自定义的Money反序列化逻辑
     *
     * @param p     JsonParser对象，用于解析JSON数据
     * @param ctxt  DeserializationContext对象，提供反序列化过程中的上下文信息
     * @return 反序列化后的Money对象
     * @throws IOException            当读取JSON数据发生IO错误时抛出
     * @throws JsonProcessingException 当处理JSON数据时发生错误时抛出
     */
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // 从JsonParser中获取字符串，并使用Money.of()方法创建带有货币单位为 "CNY" 的Money对象
        String valueAsString = p.getValueAsString();
        if (valueAsString == null) {
            return Money.zero(CurrencyUnit.of("CNY"));
        }else{
            BigDecimal decimalValue = new BigDecimal(valueAsString);
            return Money.of(CurrencyUnit.of("CNY"), decimalValue);
        }
    }
}
