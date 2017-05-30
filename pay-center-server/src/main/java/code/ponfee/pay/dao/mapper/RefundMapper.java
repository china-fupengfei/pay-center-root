package code.ponfee.pay.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import code.ponfee.pay.model.Refund;

public interface RefundMapper {

    int insert(Refund refund);

    int updateStatus(Refund refund);

    Refund getByRefundId(String refundId);
    
    Refund getRefundByRefundNo(@Param("refundType") int a, @Param("refundNo") String b);

    List<Refund> listByPayId(String payId);

    /**
     * 定时扫描
     * @param params
     * @return
     */
    List<Refund> listRefundTaskScan(Map<String, Object> params);

    /**
     * 定时扫描更新
     * @param params
     * @return
     */
    int updateRefundTaskScan(Map<String, Object> params);
}