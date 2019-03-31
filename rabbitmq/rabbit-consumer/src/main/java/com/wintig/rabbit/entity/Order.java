package com.wintig.rabbit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private String id;

    private String name;

    private String messageID;   // 存储发送消息的唯一标示

}
