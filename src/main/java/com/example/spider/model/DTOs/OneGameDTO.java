package com.example.spider.model.DTOs;

import com.example.spider.controller.Util;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OneGameDTO {
    @Column(name = "name")
    private String  name;
    @Column(name = "type")
    private String  type;
    @Min(value = Util.STAKE, message = "Stake must be at least "+Util.STAKE)
    @Column(name = "stake")
    private int stake;

}
