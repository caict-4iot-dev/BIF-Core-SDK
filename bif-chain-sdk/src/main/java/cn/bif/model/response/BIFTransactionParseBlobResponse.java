package cn.bif.model.response;

import cn.bif.exception.SdkError;
import cn.bif.model.response.result.BIFTransactionParseBlobResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BIFTransactionParseBlobResponse extends BIFBaseResponse {
    @JsonProperty(value ="result")
    private BIFTransactionParseBlobResult result;

    public BIFTransactionParseBlobResult getResult() {
        return result;
    }

    public void setResult(BIFTransactionParseBlobResult result) {
        this.result = result;
    }
    /**
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     */
    public void buildResponse(SdkError sdkError, BIFTransactionParseBlobResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     */
    public void buildResponse(int errorCode, String errorDesc, BIFTransactionParseBlobResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
