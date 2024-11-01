package com.fast.learners.platform.iam.domain.model.commands;

import com.fast.learners.platform.iam.domain.model.entities.Membership;

import java.util.List;

public record SignUpCommand(String email, String password, List<Membership> memberships) {
}
