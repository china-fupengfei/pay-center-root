package code.ponfee.tenpay.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.tenpay.model.enums.SignType;
import code.ponfee.tenpay.model.serializer.SignTypeDeserializer;

/**
 * 公用参数
 * @author fupf
 */
abstract class TenpayParams implements Serializable {

    private static final long serialVersionUID = 3521763514894148794L;

    @JsonProperty(TenpayField.SIGN_TYPE)
    @JsonDeserialize(using = SignTypeDeserializer.class)
    private SignType signType;

    @JsonProperty(TenpayField.SERVICE_VERSION)
    private String serviceVersion = "1.0";

    @JsonProperty(TenpayField.INPUT_CHARSET)
    private String inputCharset;

    private String sign;

    @JsonProperty(TenpayField.SIGN_KEY_INDEX)
    private int signKeyIndex = 1;

    @Constraint(notBlank=true)
    private String partner;

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getSignKeyIndex() {
        return signKeyIndex;
    }

    public void setSignKeyIndex(int signKeyIndex) {
        this.signKeyIndex = signKeyIndex;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

}