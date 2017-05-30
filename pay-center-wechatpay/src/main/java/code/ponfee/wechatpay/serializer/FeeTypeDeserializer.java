package code.ponfee.wechatpay.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import code.ponfee.wechatpay.model.enums.FeeType;

/**
 * 交易类型反序列化器
 */
public class FeeTypeDeserializer extends JsonDeserializer<FeeType> {

    @Override
    public FeeType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Enum.valueOf(FeeType.class, jp.getValueAsString());
    }
}
