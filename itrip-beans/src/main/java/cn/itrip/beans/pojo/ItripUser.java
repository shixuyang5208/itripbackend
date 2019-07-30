package cn.itrip.beans.pojo;

import java.io.Serializable;
import java.util.Date;

public class ItripUser implements Serializable {

            private Long id;
            private String userCode;//若是第三方登录，系统将自动生成唯一账号；自注册用户则为邮箱或者手机号
            private String userPassword;//若是第三方登录，系统将自动生成唯一密码；自注册用户则为自定义密码
            private Integer userType;//用户类型（标识：0 自注册用户 1 微信登录 2 QQ登录 3 微博登录）
            private Long flatID;//平台ID（根据不同登录用户，进行相应存入：自注册用户主键ID、微信ID、QQID、微博ID）
            private String userName;//用户昵称
            private String weChat;//微信号
            private String QQ;//qq账号
            private String weibo;//微博账号
            private String baidu;//百度账号
            private int activated;
            private Date creationDate;
            private Long createdBy;
            private Date modifyDate;
            private Long modifiedBy;//是否激活,(0 false，1 true,默认是0)

            public void setId (Long  id){
                this.id=id;
            }

            public  Long getId(){
                return this.id;
            }
            public void setUserCode (String  userCode){
                this.userCode=userCode;
            }

            public  String getUserCode(){
                return this.userCode;
            }
            public void setUserPassword (String  userPassword){
                this.userPassword=userPassword;
            }

            public  String getUserPassword(){
                return this.userPassword;
            }
            public void setUserType (Integer  userType){
                this.userType=userType;
            }

            public  Integer getUserType(){
                return this.userType;
            }
            public void setFlatID (Long  flatID){
                this.flatID=flatID;
            }

            public  Long getFlatID(){
                return this.flatID;
            }
            public void setUserName (String  userName){
                this.userName=userName;
            }

            public  String getUserName(){
                return this.userName;
            }
            public void setWeChat (String  weChat){
                this.weChat=weChat;
            }

            public  String getWeChat(){
                return this.weChat;
            }
            public void setQQ (String  QQ){
                this.QQ=QQ;
            }

            public  String getQQ(){
                return this.QQ;
            }
            public void setWeibo (String  weibo){
                this.weibo=weibo;
            }

            public  String getWeibo(){
                return this.weibo;
            }
            public void setBaidu (String  baidu){
                this.baidu=baidu;
            }

            public  String getBaidu(){
                return this.baidu;
            }

            public int getActivated() {
                return activated;
            }

            public void setActivated(int activated) {
                this.activated = activated;
            }

            public void setCreationDate (Date  creationDate){
                        this.creationDate=creationDate;
                    }

            public  Date getCreationDate(){
                return this.creationDate;
            }
            public void setCreatedBy (Long  createdBy){
                this.createdBy=createdBy;
            }

            public  Long getCreatedBy(){
                return this.createdBy;
            }
            public void setModifyDate (Date  modifyDate){
                this.modifyDate=modifyDate;
            }

            public  Date getModifyDate(){
                return this.modifyDate;
            }
            public void setModifiedBy (Long  modifiedBy){
                this.modifiedBy=modifiedBy;
            }

            public  Long getModifiedBy(){
                return this.modifiedBy;
            }

}
