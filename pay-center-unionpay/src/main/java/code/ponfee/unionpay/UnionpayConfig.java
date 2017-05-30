package code.ponfee.unionpay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.commons.resource.ResourceLoaderFacade;

/**
 * 配置
 * @author fupf
 */
final class UnionpayConfig {

    private static Logger logger = LoggerFactory.getLogger(UnionpayConfig.class);

    static String charset;
    static String merAbbr;

    // PC端支付
    static String webMerId;
    static String webMd5Key;
    
    // APP端支付
    static String appMerId;
    static String appMd5Key;

    static {
        InputStream in = null;
        try {
            in = ResourceLoaderFacade.getResource("pay/unionpay/unionpay.config.properties", UnionpayConfig.class).getStream();
            Properties props = new Properties();
            props.load(in);
            charset = props.getProperty("charset", "UTF-8");
            merAbbr = props.getProperty("mer.abbr");
            
            webMerId = props.getProperty("web.mer.id");
            webMd5Key = props.getProperty("web.md5.key");
            
            appMerId = props.getProperty("app.mer.id");
            appMd5Key = props.getProperty("app.md5.key");
        } catch(Exception e) {
            logger.error("init unionpay config error.", e);
            System.exit(1);
        } finally {
            if (in != null) try {
                in.close();
            } catch(IOException e) {
                logger.error("close unionpay props error.", e);
            }
        }
    }
}
