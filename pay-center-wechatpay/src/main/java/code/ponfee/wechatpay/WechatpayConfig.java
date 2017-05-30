package code.ponfee.wechatpay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.commons.jce.security.KeyStoreResolver;
import code.ponfee.commons.jce.security.KeyStoreResolver.KeyStoreType;
import code.ponfee.commons.resource.ResourceLoaderFacade;

/**
 * 微信支付配置
 * @author fupf
 */
class WechatpayConfig {
    private static Logger logger = LoggerFactory.getLogger(WechatpayConfig.class);

    static String appId;
    static String appIdJSAPI;
    static String appKey;
    static String mchId;
    static SSLContext sslContext;

    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = ResourceLoaderFacade.getResource("pay/wechatpay/wechat.config.properties", WechatpayConfig.class).getStream();
            props.load(in);
            in.close();
            appId = props.getProperty("appId");
            appIdJSAPI = props.getProperty("appId.jsapi");
            appKey = props.getProperty("appKey");
            mchId = props.getProperty("mchId");

            // load cert
            in = ResourceLoaderFacade.getResource(props.getProperty("keyStorePath")).getStream();
            String storePassword = props.containsKey("storePassword") ? props.getProperty("storePassword") : mchId;
            String keyPassword = props.containsKey("keyPassword") ? props.getProperty("keyPassword") : mchId;
            KeyStoreResolver resolver;
            if ("jks".equalsIgnoreCase(props.getProperty("keyStoreType"))) {
                resolver = new KeyStoreResolver(KeyStoreType.JKS, in, storePassword); // jks
            } else {
                resolver = new KeyStoreResolver(KeyStoreType.PKCS12, in, storePassword); // pfx
            }
            sslContext = resolver.getSSLContext(keyPassword);
        } catch (Exception e) {
            logger.error("init wechatpay config error", e);
            System.exit(1);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                logger.error("close wechatpay props error", e);
            }
        }
    }
}
