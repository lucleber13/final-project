package com.cbcode.final_project.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarDTO {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String model;

    @NotNull
    @Size(max = 20)
    private String color;

    @NotNull
    @Size(max = 10)
    private String regNumber;

    @NotNull
    @Size(max = 3)
    private String keysNumber;

    @NotNull
    private Long sales;

}
