package code.ponfee.pay.dao.mapper;

import java.util.List;

import code.ponfee.pay.model.PayChannel;

public interface PayChannelMapper {

    List<PayChannel> listBySource(String source);
}