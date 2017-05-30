package code.ponfee.wechatpay.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import code.ponfee.wechatpay.model.enums.TradeState;

/**
 * 交易状态反序列化器
 */
public class TradeStateDeserializer extends JsonDeserializer<TradeState> {

    @Override
    public TradeState deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Enum.valueOf(TradeState.class, jp.getValueAsString());
    }
}
