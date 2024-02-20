package com.nva.server.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Long createdDate;
    private Long lastModifiedDate;
    private Boolean isEnabled;
}
