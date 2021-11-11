package cn.bif.model.response.result.data;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
public class BIFLogInfo {
    @JsonProperty(value =  "topic")
    private String topic;

    @JsonProperty(value =  "datas")
    private String[] datas;

    /**
     *
     * @Method getTopic
     * @Params []
     * @Return java.lang.String
     *
     */
    public String getTopic() {
        return topic;
    }

    /**
     *
     * @Method setTopic
     * @Params [topic]
     * @Return void
     *
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     *
     * @Method getDatas
     * @Params []
     * @Return java.lang.String[]
     *
     */
    public String[] getDatas() {
        return datas;
    }

    /**
     *
     * @Method setDatas
     * @Params [datas]
     * @Return void
     *
     */
    public void setDatas(String[] datas) {
        this.datas = datas;
    }
}
