package com.example.spider.model.DTOs;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithoutPasswordDTO {

    private long id;
    private String email;
    private String name;


}
