package code.ponfee.tenpay;

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
 * tenpay配置
 * @author fupf
 */
class TenpayConfig {

    private static final List<String> CERT_TYPE = Arrays.asList("cer", "crt", "pem");

    private static Logger logger = LoggerFactory.getLogger(TenpayConfig.class);
    static String partner;
    static String md5Key;
    static String opUserPwd;
    static String appId;
    static String appSecret;

    static SSLContext sslContext;

    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = ResourceLoaderFacade.getResource("pay/tenpay/tenpay.config.properties", TenpayConfig.class).getStream();
            props.load(in);
            in.close();
            in = null;

            partner = props.getProperty("partner");
            md5Key = props.getProperty("key.md5");
            opUserPwd = props.getProperty("op.user.pwd");
            appId = props.getProperty("app.id");
            appSecret = props.getProperty("app.secret");

            String trustStorePath = props.getProperty("trust.store.path");
            KeyStore trustStore = null;
            if (StringUtils.isNotBlank(trustStorePath)) {
                String trustStorePwd = props.getProperty("trust.store.passwd");
                String trustStoreType = props.getProperty("trust.store.type");
                in = ResourceLoaderFacade.getResource(trustStorePath, TenpayConfig.class).getStream();

                if (CERT_TYPE.contains(trustStoreType.toLowerCase())) {
                    KeyStoreResolver resolver = new KeyStoreResolver(KeyStoreType.JKS);
                    resolver.setCertificateEntry("tenpay", X509CertUtils.loadX509Cert(in));
                    trustStore = resolver.getKeyStore();
                } else if ("jks".equalsIgnoreCase(trustStoreType)) {
                    trustStore = new KeyStoreResolver(KeyStoreType.JKS, in, trustStorePwd).getKeyStore();
                } else {
                    trustStore = new KeyStoreResolver(KeyStoreType.PKCS12, in, trustStorePwd).getKeyStore();
                }
                in.close();
                in = null;
            }

            String keyStorePath = props.getProperty("key.store.path");
            if (StringUtils.isNotBlank(keyStorePath)) {
                String keyStoreType = props.getProperty("key.store.type");
                String keyStorePwd = props.getProperty("key.store.keypwd");
                String keyPasswd = props.getProperty("key.store.passwd");
                in = ResourceLoaderFacade.getResource(keyStorePath, TenpayConfig.class).getStream();
                KeyStoreResolver store;
                if ("jks".equalsIgnoreCase(keyStoreType)) {
                    store = new KeyStoreResolver(KeyStoreType.JKS, in, keyStorePwd);
                } else {
                    store = new KeyStoreResolver(KeyStoreType.PKCS12, in, keyStorePwd);
                }
                in.close();
                in = null;
                sslContext = store.getSSLContext(keyPasswd, trustStore);
            }
        } catch (Exception e) {
            logger.error("init tenpay config error.", e);
            System.exit(1);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                logger.error("close tenpay props error.", e);
            }
        }
    }

}
