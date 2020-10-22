package com.kangqing.mybatisdemo.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String name;

    private Integer age;

    private String email;

    private String status;

    private static final long serialVersionUID = 1L;
}