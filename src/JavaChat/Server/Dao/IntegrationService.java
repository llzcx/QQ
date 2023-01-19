package JavaChat.Server.Dao;

import JavaChat.Common.Pojo.Group;
import JavaChat.Common.Pojo.GroupMember;
import JavaChat.Common.Pojo.User;
import JavaChat.Common.Utils.FileUtils;

import java.io.File;
import java.util.Vector;

/**
 * 该类包含服务端的一些整合操作
 */
public class IntegrationService {

    /**
     * (下标0)该方法获得群user表(包含头像)
     * (下标1)群头像群信息
     * (下标2)群成员信息表(member)
     * @param groupNumber
     * @return
     */
    public static Vector<Object> getIntegrationGroup(String groupNumber) throws Exception{
        Vector<Object> AllVector = new Vector<>();
        //获得user
        Vector<User> userVector = DatabaseOperation_GroupApplication.GetGroupAllUser(groupNumber);
        //获得user的头像
        for (int i = 0; i < userVector.size(); i++) {
            User user = userVector.get(i);
            File file = new File(user.getHeadPortraitPath());
            user.setHeadPortraitFile(file);
            user.setHeadPortrait_byte(FileUtils.FileChangeToByte(file));
        }
        AllVector.add(userVector);
        //获得群头像和群信息
        Vector<String> vString = new Vector<>();
        vString.add(groupNumber);
        Group group = DatabaseOperation_GroupList.ReturnGroupListInformation(vString).get(0);
        File file = new File(group.getGroupHeadPortraitFilePath());
        group.setGroupHeadPortraitFile(file);
        group.setGroupHeadPortraitFileBytes(FileUtils.FileChangeToByte(file));
        AllVector.add(group);
        //获得群成员
        Vector<GroupMember> memberVector = DatabaseOperation_GroupApplication.GetGroupAllGroupMember(groupNumber);
        AllVector.add(memberVector);
        return AllVector;
    }
}
