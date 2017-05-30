package code.ponfee.wechatpay.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import code.ponfee.wechatpay.model.enums.TradeType;

/**
 * 交易类型反序列化器
 */
public class TradeTypeDeserializer extends JsonDeserializer<TradeType> {

    @Override
    public TradeType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Enum.valueOf(TradeType.class, jp.getValueAsString());
    }
    
}
