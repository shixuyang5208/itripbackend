package cn.itrip.beans.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 前端输入-修改订单VO
 * Created by donghai on 2017/5/18.
 */
@ApiModel(value = "ItripModifyHotelOrderVO", description = "修改订单VO")
public class ItripModifyHotelOrderVO implements Serializable {
    @ApiModelProperty("[必填，主键]")
    private Long id;
    @ApiModelProperty("[必填，注：接收数字类型] 支付方式(:1:支付宝 2:微信 3:到店付)")
    private Integer payType;
    private Integer orderType;
    private String orderNo;
    private Long hotelId;
    private String hotelName;//酒店名称
    private Long roomId;//房间编号
    private Integer count;
    private Integer bookingDays;
    private Date checkInDate;//入住日期
    private Date checkOutDate;//退房日期
    private String noticePhone;//联系电话
    private String noticeEmail;//联系邮箱
    private String specialRequirement;//特殊需求
    private Integer isNeedInvoice;//是否需要发票
    private Integer invoiceType;//发票类型
    private String invoiceHead;//发票抬头
    private String linkUserName;//旅客姓名
    private Integer bookType;
    private List<ItripOrderLinkUserVo> itripOrderLinkUserList;

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getBookingDays() {
        return bookingDays;
    }

    public void setBookingDays(Integer bookingDays) {
        this.bookingDays = bookingDays;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getNoticePhone() {
        return noticePhone;
    }

    public void setNoticePhone(String noticePhone) {
        this.noticePhone = noticePhone;
    }

    public String getNoticeEmail() {
        return noticeEmail;
    }

    public void setNoticeEmail(String noticeEmail) {
        this.noticeEmail = noticeEmail;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    public Integer getIsNeedInvoice() {
        return isNeedInvoice;
    }

    public void setIsNeedInvoice(Integer isNeedInvoice) {
        this.isNeedInvoice = isNeedInvoice;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }

    public Integer getBookType() {
        return bookType;
    }

    public void setBookType(Integer bookType) {
        this.bookType = bookType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<ItripOrderLinkUserVo> getItripOrderLinkUserList() {
        return itripOrderLinkUserList;
    }

    public void setItripOrderLinkUserList(List<ItripOrderLinkUserVo> itripOrderLinkUserList) {
        this.itripOrderLinkUserList = itripOrderLinkUserList;
    }
}
