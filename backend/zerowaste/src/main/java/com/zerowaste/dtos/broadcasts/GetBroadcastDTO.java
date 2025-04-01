package com.zerowaste.dtos.broadcasts;

public class GetBroadcastDTO {
    private Long id;
    private String name;
    private String sendType;
    

    public GetBroadcastDTO() {}

    public GetBroadcastDTO(Long id, String name, String sendType) {
        this.id = id;
        this.name = name;
        this.sendType = sendType;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}


