package code.ponfee.pay;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.model.Result;
import code.ponfee.pay.service.IPayService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class TestPayService {

    @Resource
    private IPayService payService;
    
    @Before
    public void setup() {
    }
    
    @After
    public void teardown() {
        
    }

    @Test
    public void test() {
        Map<String, String> params = new HashMap<>();
        params.put("expireTime", "20160409121212");
        Result<?> result = payService.pay(params);
        System.out.println(Jsons.NORMAL.stringify(result));
    }
    
    @Test
    public void test1() {
        Result<?> result = payService.listPayChannelBySource("IOS");
        System.out.println(Jsons.NORMAL.stringify(result));
    }

}