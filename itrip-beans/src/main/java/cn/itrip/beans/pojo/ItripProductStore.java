package cn.itrip.beans.pojo;

import java.io.Serializable;
import java.util.Date;

public class ItripProductStore implements Serializable {

            private Long id;
            private Integer productType;//商品类型(0:旅游产品 1:酒店产品 2:机票产品)
            private Long productId;//商品id
            private Integer store;//商品库存
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
            public void setProductType (Integer  productType){
                this.productType=productType;
            }

            public  Integer getProductType(){
                return this.productType;
            }
            public void setProductId (Long  productId){
                this.productId=productId;
            }

            public  Long getProductId(){
                return this.productId;
            }
            public void setStore (Integer  store){
                this.store=store;
            }

            public  Integer getStore(){
                return this.store;
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
