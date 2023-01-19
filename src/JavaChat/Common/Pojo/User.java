package JavaChat.Common.Pojo;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer Id;
    private String Name;
    private String AccountNumber;
    private String AccountPassword;
    private String Age;
    private String Gender;
    private String Birthday;
    private byte[] HeadPortrait_byte;//头像
    private String HeadPortraitPath;//头像图片在服务器中的路径
    private File HeadPortraitFile;
    private String PersonalSignature;//个性签名
    private String School;
    private String Email;
    private String VerificationCode;
    private String FileSavePath;
    private String Online_Offline_State = "";
    public User(){}

    public User(String name,String Acc){
        this.AccountNumber = Acc;
        this.Name = name;
    }

    public String getOnline_Offline_State() {
        return Online_Offline_State;
    }

    public void setOnline_Offline_State(String online_Offline_State) {
        Online_Offline_State = online_Offline_State;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountPassword() {
        return AccountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        AccountPassword = accountPassword;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public byte[] getHeadPortrait_byte() {
        return HeadPortrait_byte;
    }

    public void setHeadPortrait_byte(byte[] headPortrait_byte) {
        HeadPortrait_byte = headPortrait_byte;
    }

    public String getHeadPortraitPath() {
        return HeadPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        HeadPortraitPath = headPortraitPath;
    }

    public String getPersonalSignature() {
        return PersonalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        PersonalSignature = personalSignature;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    public String getFileSavePath() {
        return FileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        FileSavePath = fileSavePath;
    }

    public File getHeadPortraitFile() {
        return HeadPortraitFile;
    }

    public void setHeadPortraitFile(File headPortraitFile) {
        HeadPortraitFile = headPortraitFile;
    }
}

