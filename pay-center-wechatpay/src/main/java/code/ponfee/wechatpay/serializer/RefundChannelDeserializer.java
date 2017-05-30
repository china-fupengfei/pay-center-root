package code.ponfee.wechatpay.serializer;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import code.ponfee.wechatpay.model.enums.RefundChannel;

/**
 * 退款渠道反序列化器
 * @since 1.0.0
 */
public class RefundChannelDeserializer extends JsonDeserializer<RefundChannel> {

    @Override
    public RefundChannel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String val = jp.getValueAsString();
        return StringUtils.isEmpty(val) ? null : Enum.valueOf(RefundChannel.class, val);
    }
}
