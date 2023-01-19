package JavaChat.Common.Transfer;

/**
 * 文件状态枚举类
 */
public enum FileMessageState implements StateType{
    //暂时未处理
    UNTREATED(0,"暂时未处理"),
    //已经同意了
    AGREE(1,"同意"),
    //已经拒绝了
    REJECT(2,"拒绝");
    public final Integer StateValue;
    public final String Chinese;
    FileMessageState(Integer stateValue,String chinese) {
        this.StateValue = stateValue;
        this.Chinese = chinese;
    }


    @Override
    public String toString() {
        return StateValue.toString();
    }

    @Override
    public String toChinese() {
        return Chinese.toString();
    }
}
