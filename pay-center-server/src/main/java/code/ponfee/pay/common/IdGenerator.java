package code.ponfee.pay.common;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import code.ponfee.commons.util.Strings;
import code.ponfee.pay.enums.BizType;
import code.ponfee.pay.enums.ChannelType;
import code.ponfee.pay.enums.TradeType;
import code.ponfee.sequence.service.ISequenceService;

/**
 * ID生成器
 * @author fupf
 */
@Component("idGenerator")
public class IdGenerator {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    @Resource
    private ISequenceService keyService;

    @Value("${payment.sequence.name}")
    private String sequence;

    @Value("${deploy.env.isonline:false}")
    private boolean isOnline;

    @Value("${generate.id.fixed.size:9}")
    private int idFixedSize;

    /**
     * 支付编号生成：共24位
     * @param bizType
     * @param channelType
     * @return
     */
    public String genPaymentId(BizType bizType, ChannelType channelType) {
        return generateId(TradeType.PAY, channelType, bizType);
    }

    /**
     * 退款编号生成：共24位
     * @param bizType
     * @param channelType
     * @return
     */
    public String genRefundId(BizType bizType, ChannelType channelType) {
        return generateId(TradeType.REFUND, channelType, bizType);
    }

    private String generateId(TradeType tradeType, ChannelType channelType, BizType bizType) {
        StringBuffer buf = new StringBuffer(DATE_FORMAT.format(new Date())); // 时间8位
        buf.append(tradeType.ordinal() + 1); // 交易类型1位
        buf.append(leftPad(channelType.ordinal() + 1, 2)); // 渠道类型2位
        buf.append(leftPad(bizType.ordinal() + 1, 2)); // 业务类型2位
        buf.append(leftPad(keyService.nextValue(sequence), idFixedSize)); // 自增编号9位
        // 是否线上环境1位
        if (isOnline) {
            buf.append(1);
        } else {
            buf.append(0);
        }
        buf.append(Strings.iemeCode(buf.toString())); // 校验码1位
        return buf.toString();
    }

    private String leftPad(long value, int size) {
        value = value % (long) Math.pow(10, size);
        return StringUtils.leftPad(String.valueOf(value), size, "0");
    }

    public static void main(String[] args) {
        /*long value = 21321;
        int size = 9;
        long maxValue = (long) Math.pow(10, size);
        value = value % maxValue;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            set.add(genIEMECode(ObjectUtils.uuid32()));
        }
        //System.out.println(ObjectUtils.toString(set));
        
        System.out.println(TradeType.PAY.ordinal());*/

        System.out.println(213453245 % 1000000000);
    }
}
