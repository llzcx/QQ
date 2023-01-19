package JavaChat.Common.Transfer;

public enum GroupApplicationState {
    //暂时未处理
    UNTREATED(0,"暂时未处理"),
    //已经同意了
    AGREE(1,"同意"),
    //已经拒绝了
    REJECT(2,"拒绝");
    //xx已经退出群聊

    public final Integer StateValue;
    public final String Chinese;
    GroupApplicationState(Integer stateValue,String chinese) {
        this.StateValue = stateValue;
        this.Chinese = chinese;
    }


    @Override
    public String toString() {
        return StateValue.toString();
    }
}
