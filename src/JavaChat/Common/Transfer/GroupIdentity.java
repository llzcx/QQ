package JavaChat.Common.Transfer;

import java.security.PublicKey;

public enum GroupIdentity implements IdentityType{
    //群主GroupLeader
    GROUP_LEADER(0,"群主"),
    //管理员Administrators
    ADMINISTRATORS(1,"管理员"),
    //普通群员Ordinary group members
    ORDINARY_GROUP_MEMBERS(2,"普通群员");

    public final Integer IdentityValue;
    public final String Chinese;
    GroupIdentity(Integer stateValue, String chinese) {
        this.IdentityValue = stateValue;
        this.Chinese = chinese;
    }


    @Override
    public String toString() {
        return IdentityValue.toString();
    }
    @Override
    public String toChinese() {
        return Chinese.toString();
    }
}
