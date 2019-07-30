package cn.itrip.beans.pojo;

import java.io.Serializable;
import java.util.Date;

public class ItripUserLinkUser implements Serializable {

            private Long id;
            private String linkUserName;//常用联系人姓名
            private Integer linkIdCardType;//证件类型：(0-身份证，1-护照，2-学生证，3-军人证，4-驾驶证，5-旅行证)
            private String linkIdCard;//证件号码
            private String linkPhone;//常用联系人电话
            private Long userId;
            //@JSONField(format="yyyy-MM-dd")
            private Date creationDate;
            private Long createdBy;
            private Date modifyDate;
            private Long modifiedBy;

            public void setId (Long  id){
                this.id=id;
            }

            public  Long getId(){
                return this.id;
            }
            public void setLinkUserName (String  linkUserName){
                this.linkUserName=linkUserName;
            }

            public  String getLinkUserName(){
                return this.linkUserName;
            }
            public void setLinkIdCard (String  linkIdCard){
                this.linkIdCard=linkIdCard;
            }

            public Integer getLinkIdCardType() {
                return linkIdCardType;
            }

            public void setLinkIdCardType(Integer linkIdCardType) {
                this.linkIdCardType = linkIdCardType;
            }

            public  String getLinkIdCard(){
                return this.linkIdCard;
            }
            public void setLinkPhone (String  linkPhone){
                this.linkPhone=linkPhone;
            }

            public  String getLinkPhone(){
                return this.linkPhone;
            }
            public void setUserId (Long  userId){
                this.userId=userId;
            }

            public  Long getUserId(){
                return this.userId;
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
