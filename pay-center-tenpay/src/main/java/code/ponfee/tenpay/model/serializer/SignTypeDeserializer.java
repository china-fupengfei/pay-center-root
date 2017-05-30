package code.ponfee.tenpay.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import code.ponfee.tenpay.model.enums.SignType;

/**
 * 签名类型反序列化
 * @author fupf
 */
public class SignTypeDeserializer extends JsonDeserializer<SignType> {

    @Override
    public SignType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return SignType.from(jp.getValueAsString());
    }
}