package JavaChat.Common.Transfer;

public enum GroupNoticeType {
    //群主GroupLeader
    XX_ENTER(0,"某某进群"),
    //管理员Administrators
    XX_EXIT(1,"某某退出"),
    //普通群员Ordinary group members
    XX_REMOVE_XX(2,"某某被某某踢出去了"),
    XX_BE_XX_SET_ADMINISTRATOR(3,"某某被设置为了管理员"),
    XX_BE_XX_DEMOTION(4,"某某被某某降职"),
    //XX_GROUP_ADMINISTRATOR_BE_DISSOLUTION(5,"某某群被解散了");
    XX_GROUP_ADMINISTRATOR_BE_DISSOLUTION(5,"某某被某某降职");


    public final Integer IdentityValue;
    public final String Chinese;
    GroupNoticeType(Integer stateValue, String chinese) {
        this.IdentityValue = stateValue;
        this.Chinese = chinese;
    }

    @Override
    public String toString() {
        return IdentityValue.toString();
    }

    public String toChinese() {
        return Chinese.toString();
    }
}
