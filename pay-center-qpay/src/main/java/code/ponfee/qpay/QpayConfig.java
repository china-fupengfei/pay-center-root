package code.ponfee.qpay;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import code.ponfee.commons.jce.cert.X509CertUtils;
import code.ponfee.commons.jce.security.KeyStoreResolver;
import code.ponfee.commons.jce.security.KeyStoreResolver.KeyStoreType;
import code.ponfee.commons.resource.ResourceLoaderFacade;

/**
 * QpayConfig
 * @author fupf
 */
class QpayConfig {
    private static final List<String> CERT_TYPE = Arrays.asList("cer", "crt", "pem");
    private static Logger logger = LoggerFactory.getLogger(QpayConfig.class);

    static String mchId;
    static String md5Key;
    static String opUserId;
    static String opUserPwd;
    static String appid;
    static String appKey;
    static SSLContext sslContext;

    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = ResourceLoaderFacade.getResource("pay/qpay/qpay.config.properties", QpayConfig.class).getStream();
            props.load(in);
            in.close();
            in = null;

            mchId = props.getProperty("mch.id");
            md5Key = props.getProperty("md5.key");
            opUserId = props.getProperty("op.user.id");
            opUserPwd = props.getProperty("op.user.pwd");
            appid = props.getProperty("app.id");
            appKey = props.getProperty("app.key");

            /** 添加受信任的证书库 */
            String trustStorePath = props.getProperty("trust.store.path");
            KeyStore trustStore = null;
            if (StringUtils.isNotBlank(trustStorePath)) {
                String trustStorePwd = props.getProperty("trust.store.passwd");
                String trustStoreType = props.getProperty("trust.store.type");
                in = ResourceLoaderFacade.getResource(trustStorePath, QpayConfig.class).getStream();

                if (CERT_TYPE.contains(trustStoreType.toLowerCase())) {
                    KeyStoreResolver store = new KeyStoreResolver(KeyStoreType.JKS); // cer, crt, pem
                    store.setCertificateEntry("qpay-" + mchId, X509CertUtils.loadX509Cert(in));
                    trustStore = store.getKeyStore();
                } else if ("jks".equalsIgnoreCase(trustStoreType)) {
                    trustStore = new KeyStoreResolver(KeyStoreType.JKS, in, trustStorePwd).getKeyStore(); // jks
                } else {
                    trustStore = new KeyStoreResolver(KeyStoreType.PKCS12, in, trustStorePwd).getKeyStore(); // pfx
                }
                in.close();
                in = null;
            }

            /** 加载商户证书（密钥库） */
            String keyStorePath = props.getProperty("key.store.path");
            if (StringUtils.isNotBlank(keyStorePath)) {
                String keyStoreType = props.getProperty("key.store.type");
                String keyStorePwd = props.getProperty("key.store.keypwd");
                String keyPasswd = props.getProperty("key.store.passwd");

                in = ResourceLoaderFacade.getResource(keyStorePath, QpayConfig.class).getStream();
                KeyStoreResolver store;
                if ("jks".equalsIgnoreCase(keyStoreType)) {
                    store = new KeyStoreResolver(KeyStoreType.JKS, in, keyStorePwd); // jks
                } else {
                    store = new KeyStoreResolver(KeyStoreType.PKCS12, in, keyStorePwd); // pfx
                }
                in.close();
                in = null;
                sslContext = store.getSSLContext(keyPasswd, trustStore);
            }
        } catch (Exception e) {
            logger.error("init qpay config error.", e);
            System.exit(1);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                logger.error("close qpay props error.", e);
            }
        }
    }

}
