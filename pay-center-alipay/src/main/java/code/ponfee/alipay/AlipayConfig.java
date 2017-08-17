package code.ponfee.alipay;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.commons.jce.security.RSACryptor;
import code.ponfee.commons.resource.ResourceLoaderFacade;

/**
 * ali支付配置
 * @author fupf
 */
class AlipayConfig {

    private static Logger logger = LoggerFactory.getLogger(AlipayConfig.class);

    static String partner;
    static String md5Key;
    static RSAPrivateKey ptnPriKey;
    static RSAPublicKey aliPubKey;

    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = ResourceLoaderFacade.getResource("pay/alipay/alipay.config.properties", AlipayConfig.class).getStream();
            props.load(in);

            partner = props.getProperty("partner");
            md5Key = props.getProperty("key.md5");
            ptnPriKey = RSACryptor.fromPkcs8PrivateKey(props.getProperty("key.rsa.partner.private"));
            aliPubKey = RSACryptor.fromPkcs8PublicKey(props.getProperty("key.rsa.ali.public"));
        } catch(Exception e) {
            logger.error("init alipay config error.", e);
            System.exit(1);
        } finally {
            if (in != null) try {
                in.close();
            } catch(IOException e) {
                logger.error("close alipay props error.", e);
            }
        }
    }
}