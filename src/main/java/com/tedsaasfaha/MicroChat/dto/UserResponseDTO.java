
package com.tedsaasfaha.MicroChat.dto;

import com.tedsaasfaha.MicroChat.controller.UserController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;


@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {
    private Long id;
    private String name;
    private String email;
    private String role;

    public void addLinks() {
        // Self link: /api/v1/users/{id}
        this.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserById(this.id))
                .withSelfRel());

        // Collection link: /api/v1/users
        this.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllActiveUsers(0, 10))
                .withRel("users"));
    }
}
//