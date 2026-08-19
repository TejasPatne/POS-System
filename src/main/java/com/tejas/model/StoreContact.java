package com.tejas.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreContact {
    private String address;
    private String phone;

    @Email(message = "By order of the Peaky Blinders: enter a valid email.")
    private String email;
}
