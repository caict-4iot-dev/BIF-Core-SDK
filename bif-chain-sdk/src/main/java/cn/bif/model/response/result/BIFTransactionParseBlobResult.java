package cn.bif.model.response.result;

import cn.bif.model.response.result.data.BIFOperationFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BIFTransactionParseBlobResult {
    @JsonProperty(value = "source_address")
    private String sourceAddress;

    @JsonProperty(value = "fee_limit")
    private Long feeLimit;

    @JsonProperty(value = "gas_price")
    private Long gasPrice;

    @JsonProperty(value = "nonce")
    private Long nonce;

    @JsonProperty(value = "domain_id")
    private Integer domainId;

    @JsonProperty(value = "operations")
    private BIFOperationFormat[] operations;

    @JsonProperty(value = "chain_id")
    private Long chainId;

    @JsonProperty(value = "remarks")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public Long getFeeLimit() {
        return feeLimit;
    }

    public void setFeeLimit(Long feeLimit) {
        this.feeLimit = feeLimit;
    }

    public Long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public BIFOperationFormat[] getOperations() {
        return operations;
    }

    public void setOperations(BIFOperationFormat[] operations) {
        this.operations = operations;
    }

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }
}
