package com.example.spider.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDTO {
    private long id;

    @Pattern(regexp ="^[A-Za-z]{2,20}$",message = "First name must be between 2 and 20 letters")
    private String name;
    @Email(message = "Invalid email")
    private String email;

}
