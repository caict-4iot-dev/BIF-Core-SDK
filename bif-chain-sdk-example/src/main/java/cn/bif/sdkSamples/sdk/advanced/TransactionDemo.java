package cn.bif.sdkSamples.sdk.advanced;

import cn.bif.api.BIFSDK;
import cn.bif.common.Constant;
import cn.bif.common.JsonUtils;
import cn.bif.common.SampleConstant;
import cn.bif.exception.SDKException;
import cn.bif.model.request.BIFAccountGetNonceRequest;
import cn.bif.model.request.BIFTransactionSerializeRequest;
import cn.bif.model.request.BIFTransactionSubmitRequest;
import cn.bif.model.request.operation.BIFBaseOperation;
import cn.bif.model.request.operation.BIFGasSendOperation;
import cn.bif.model.response.BIFAccountGetNonceResponse;
import cn.bif.model.response.BIFTransactionSerializeResponse;
import cn.bif.model.response.BIFTransactionSubmitResponse;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.utils.hex.HexFormat;
import org.redisson.Redisson;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 获取分布式锁
 */
public class TransactionDemo {
    private static BIFSDK sdk = BIFSDK.getInstance(SampleConstant.SDK_INSTANCE_URL);
    private static Redisson redisson = RedissonManager.getRedisson();

    public static void main(String[] args) {
        int N = 4;
        //参数
        String senderAddress="did:bid:efnVUgqQFfYeu97ABf6sGm3WFtVXHZB2";
        String senderPrivateKey="priSPKkWVk418PKAS66q4bsiE2c4dKuSSafZvNWyGGp2sJVtXL";

        String senderAddress1="did:bid:efLrPu7LNR4YwA5M1Kfx6BYb1JP7aPKp";
        String senderPrivateKey1="priSPKteVqGoNgtKE68ZjNHAbGJsNvV9nTBkTLMYTGhVjsBY5R";

        String senderAddress2="did:bid:efBdagu8sVkJWEw5kLt1w69bxa85Kuag";
        String senderPrivateKey2="priSPKmCQMrjCcRgV3u2VsYhujf7QsG7Kr6Tgm94AbzCge46d8";
        //账号集合
        List<String> availableAccAddr = new ArrayList<String>();
        availableAccAddr.add(senderAddress+";"+senderPrivateKey);
        availableAccAddr.add(senderAddress1+";"+senderPrivateKey1);
        availableAccAddr.add(senderAddress2+";"+senderPrivateKey2);
        //目的地址
        String destAddress="did:bid:efXkBsC2nQN6PJLjT9nv3Ah7S3zJt2WW";
        Long feeLimit=1000000L;
        Long gasPrice=100L;
        //交易对象
        BIFGasSendOperation gasSendOperation= new BIFGasSendOperation();
        gasSendOperation.setAmount(1L);
        gasSendOperation.setDestAddress(destAddress);
        for(int i=0;i<N;i++){
            new transaction(availableAccAddr,feeLimit,gasPrice,0,gasSendOperation).start();
        }

        for(int i=0;i<N;i++){
            new transaction(availableAccAddr,feeLimit,gasPrice,0,gasSendOperation).start();
        }
        System.out.println("END");
    }

    static class transaction extends Thread{
        List<String> availableAccAddr;
        Long feeLimit;
        Long gasPrice;
        BIFBaseOperation operation;
        Integer domainId;
        public transaction(List<String> availableAccAddr,Long feeLimit,Long gasPrice,Integer domainId,BIFBaseOperation operation ) {
            this.availableAccAddr = availableAccAddr;
            this.feeLimit = feeLimit;
            this.gasPrice = gasPrice;
            this.domainId = domainId;
            this.operation = operation;
        }

        @Override
        public void run() {
            //随机获取交易账号
            int index = new Random().nextInt(availableAccAddr.size());
            String senderAddress=availableAccAddr.get(index).split(";")[0];
            String senderPrivateKey=availableAccAddr.get(index).split(";")[1];
            //加锁
            RedissonLock.acquire(senderAddress);
            System.out.println("线程"+ Thread.currentThread().getName() +"获得分布式锁:"+senderAddress);
            try {
                //获取账号nonce值
                Long nonce=0L;
                BIFAccountGetNonceRequest request = new BIFAccountGetNonceRequest();
                request.setAddress(senderAddress);
                RMap<Object, Object> redisHash = redisson.getMap(senderAddress);
                if(!redisHash.isEmpty()){
                    nonce=Long.parseLong(redisHash.get("nonce").toString());
                }else{
//                    // 调用getNonce接口
                    BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(request);
                    if (0 == response.getErrorCode()) {
                        nonce=response.getResult().getNonce()+1;
                    }
                }
                //序列化交易
                BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
                serializeRequest.setSourceAddress(senderAddress);
                serializeRequest.setNonce(nonce);
                serializeRequest.setFeeLimit(feeLimit);
                serializeRequest.setGasPrice(gasPrice);
                serializeRequest.setOperation(operation);
                // 调用buildBlob接口
                BIFTransactionSerializeResponse serializeResponse = sdk.getBIFTransactionService().BIFSerializable(serializeRequest);
                System.out.println("serializeResponse:"+ JsonUtils.toJSONString(serializeResponse.getResult()));
                if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
                    throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
                }
                String transactionBlob = serializeResponse.getResult().getTransactionBlob();
                System.out.println("transactionBlob:"+transactionBlob);
                //签名交易
                byte[] signBytes = PrivateKeyManager.sign(HexFormat.hexToByte(transactionBlob), senderPrivateKey);
                String publicKey = PrivateKeyManager.getEncPublicKey(senderPrivateKey);
                //提交交易
                BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
                submitRequest.setSerialization(transactionBlob);
                submitRequest.setPublicKey(publicKey);
                submitRequest.setSignData(HexFormat.byteToHex(signBytes));
                // 调用bifSubmit接口
                BIFTransactionSubmitResponse transactionSubmitResponse = sdk.getBIFTransactionService().BIFSubmit(submitRequest);
                if (transactionSubmitResponse.getErrorCode() == 0) {
                    System.out.println(senderAddress+ " ,hash: "+transactionSubmitResponse.getResult().getHash());
                    //更新nonce值
                    nonce=nonce+1;
                    redisHash.put("nonce",Long.toString(nonce));
                    RedissonLock.release(senderAddress);
                    System.out.println("线程"+Thread.currentThread().getName()+"释放分布式锁");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

