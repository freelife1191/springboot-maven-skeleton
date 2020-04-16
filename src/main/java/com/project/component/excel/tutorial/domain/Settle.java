package com.project.component.excel.tutorial.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by KMS on 05/09/2019.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Settle {
    private int no;
    private String siteCode;
    private String lotName;
    private String type;
    private String issue;
    private LocalDateTime appltime;
    private LocalDateTime applordernum;
    private String appluser;
    private String applcard;
    private String applnum;
    private int price;
    private int shareny;
    private String status;
    private String tid;
    private String productname;
    private String parkingType;
    private String createId;
    private LocalDateTime createTime;

    @Builder
    public Settle(int no, String siteCode, String lotName, String type, String issue, LocalDateTime appltime, LocalDateTime applordernum, String appluser, String applcard, String applnum, int price, int shareny, String status, String tid, String productname, String parkingType, String createId, LocalDateTime createTime) {
        this.no = no;
        this.siteCode = siteCode;
        this.lotName = lotName;
        this.type = type;
        this.issue = issue;
        this.appltime = appltime;
        this.applordernum = applordernum;
        this.appluser = appluser;
        this.applcard = applcard;
        this.applnum = applnum;
        this.price = price;
        this.shareny = shareny;
        this.status = status;
        this.tid = tid;
        this.productname = productname;
        this.parkingType = parkingType;
        this.createId = createId;
        this.createTime = createTime;
    }
}
